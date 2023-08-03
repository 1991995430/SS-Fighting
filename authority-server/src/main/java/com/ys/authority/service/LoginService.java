package com.ys.authority.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.ys.authority.controller.login.param.AccessReq;
import com.ys.authority.controller.login.param.AuthParam;
import com.ys.authority.controller.login.param.ValidAccessTokenReq;
import com.ys.authority.controller.login.vo.LoginResultVO;
import com.ys.authority.entity.pojo.TokenInfo;
import com.ys.authority.mapper.SysRoleMapper;
import com.ys.authority.mapper.SysUserAssociateRoleMapper;
import com.ys.authority.mapper.SysUserMapper;
import com.ys.authority.mapper.dao.SysRole;
import com.ys.authority.mapper.dao.SysUser;
import com.ys.authority.mapper.dao.SysUserAssociateRole;
import com.ys.authority.utils.VerifyCodeImageUtil;
import com.ys.common.utils.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class LoginService {
    private static final long VERIFY_CODE_TIMEOUT_MINUTE = 5;

    private static final int randomStrNum = 4; //验证码字符个数

    private static final String randomString = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWSYZ";

    private static final String[] phoneString = {"android", "iphone", "ipad", "ipod", "ios"};

    private static Random random = new Random();

    private static Gson gson = new Gson();

    @Value("${token-cfg.timeout:20}")
    private int timeout;

    @Value("${token-cfg.phone-timeout:1440}")
    private int phoneTimeout;

    @Resource
    SysUserMapper sysUserMapper;

    @Resource
    SysRoleMapper sysRoleMapper;

    @Resource
    SysUserAssociateRoleMapper sysUserAssociateRoleMapper;

    @Resource
    UserService userService;

    private final RedisTemplate<String, String> verifyRedisTemplate;

    private final StringRedisTemplate accessTokenStringRT;

    private final UrlWhiteListService urlWhiteListService;

    @Autowired
    public LoginService(@Qualifier("verify-pool") RedisTemplate<String, String> verifyRedisTemplate,
                        @Qualifier("access-token") StringRedisTemplate accessTokenStringRT,
                        UrlWhiteListService urlWhiteListService) {
        super();
        this.verifyRedisTemplate = verifyRedisTemplate;
        this.accessTokenStringRT = accessTokenStringRT;
        this.urlWhiteListService = urlWhiteListService;
    }

    /**
     * 用户登录接口
     *
     * @param authParams    登录参数
     * @param isManageLogin 是否管理员登录接口
     * @return 登录结果
     */
    public RestResult<LoginResultVO>  login(AuthParam authParams, boolean isManageLogin) {
        String userName = authParams.getUserName();
        String password = authParams.getPassword();
        String loginStamp = authParams.getLoginStamp();
        String verifyCode = authParams.getVerifyCode();
        LoginResultVO resultVO = new LoginResultVO();
        if (userName == null || password == null || loginStamp == null || verifyCode == null
                || authParams.getServiceId() == null) {
            return resultVO.error(RestResult.CODE_PARAM_ERR, "参数缺失");
        }
        boolean verifyCheckResult = checkVerifyCode(loginStamp, verifyCode);
        if (!verifyCheckResult) {
            return resultVO.error(RestResult.CODE_PARAM_ERR, "验证码错误或超时");
        }
        SysUser sysUser = userService.checkUserPassword(authParams);
        if (sysUser == null) {
            return resultVO.error(RestResult.CODE_AUTH_ERR, "用户名或密码错误");
        }
        if (!checkManageLoginCondition(sysUser, isManageLogin)) {
            return resultVO.error(RestResult.CODE_AUTH_ERR, "仅管理员用户可登录");
        }
        String accessToken = getAccessToken(authParams);
        log.info("Success give token: {}", accessToken);
        resultVO.setAccessToken(accessToken);
        resultVO.setUserName(userName);
        resultVO.setMobileNumber(sysUser.getMobileNumber());
        resultVO.setEmail(sysUser.getEmail());
        resultVO.setServiceId(sysUser.getServiceId());
        resultVO.setRoleIdList(getRoleListByUserId(sysUser.getId()));
        resultVO.setDeptId(sysUser.getDeptId());
        resultVO.setDeptCode(sysUser.getDeptCode());
        resultVO.setId(sysUser.getId());
        return resultVO.success();
    }

    public void logout(String accessToken) {
        Boolean result = accessTokenStringRT.delete(accessToken);
        log.info("user token {} delete result: {}", accessToken, result);
    }

    public void getVerifyCodeImage(String loginStamp, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("image/png");
        response.setHeader("Expire", "0");
        response.setHeader("Pragma", "no-cache");
        try {
            String verifyCode = generateVerifyCode(loginStamp);
            log.info("[AuthController][getVerifyCodeImage]stamp:{}, verify code:{}", loginStamp, verifyCode);
            BufferedImage verifyImage = VerifyCodeImageUtil.generateImage(verifyCode);
            ImageIO.write(verifyImage, "PNG", response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkVerifyCode(String loginStamp, String verifyCode) {
        String savedCode = verifyRedisTemplate.opsForValue().get(redisVerifyCodeKey(loginStamp));
        if (savedCode == null) {
            log.debug("[checkVerifyCode]No verify code in cache of {}", loginStamp);
            return false;   // 验证码不存在，或超时了
        }
        if (!savedCode.toLowerCase().equals(verifyCode.toLowerCase())) {
            log.info("[checkVerifyCode]Fail to check verify code. source:{}-{} cached:{}",
                    loginStamp, verifyCode, savedCode);
            return false;
        }
        verifyRedisTemplate.delete(redisVerifyCodeKey(loginStamp));
        return true;
    }

    public SysUser validateToken(@NotNull String accessToken) {
        log.info("[validateToken]access-token:{}", accessToken);
        TokenInfo ac = getTokenInfo(accessToken);
        if (ac == null) {
            log.error("[validateToken]Fail to get TokenInfo");
            return null;
        }
        return ac.getSysUserInfo();
    }

    public RestResult<Map<String, Object>> checkAccessToken(AccessReq req) {
        String token = req.getToken();
        TokenInfo tokenInfo = getTokenInfo(token);
        if (tokenInfo == null) {
            log.error("[checkAccessToken]Fail to get TokenInfo by {}.", token);
            return RestResult.ret(RestResult.CODE_AUTH_ERR, RestResult.AUTH_ERR);
        }
        // log.info("[checkAccessToken]found token, role number: {}", tokenInfo.getSysRoleList().size());
        // tokenInfo.setOverTime(LocalDateTime.now().plusMinutes(tokenInfo.getTimeout()));
        tokenInfo.setOverTime(LocalDateTime.now().plusMinutes(1L));
        Duration du = Duration.between(LocalDateTime.now(), tokenInfo.getOverTime());
        accessTokenStringRT.opsForValue().set(token, gson.toJson(tokenInfo), du);
        Map<String, Object> resData = new HashMap<>();
        resData.put("userId", tokenInfo.getUserId());
        resData.put("userInfo", gson.toJson(tokenInfo.getSysUserInfo()));
        // resData.put("roleInfoList", gson.toJson(tokenInfo.getSysRoleList()));
        // resData.put("roleInfo", gson.toJson(tokenInfo.getSysRoleList().get(0)));
        log.info("[checkAccessToken]found resData size: {}", resData.size());
        /*if (!urlWhiteListService.checkUrlWriteList(req.getUrl(), tokenInfo.getSysRoleList())) {
            log.error("[checkAccessToken]Fail to check url: {}", req.getUrl());
            return RestResult.ret(RestResult.CODE_AUTH_ERR, RestResult.AUTH_ERR);
        }*/
        RestResult<Map<String, Object>> result = new RestResult<>();
        result.setData(resData);
        return result;
    }

    public RestResult<String> validAccessToken(ValidAccessTokenReq req) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SysUser.fName, req.getUserName());
        queryWrapper.eq(SysUser.fServiceId, req.getServiceId());
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        if (sysUser == null) {  // 内部环境不检查用户存在性，这里先检查一下，会造成重复查询数据库
            return RestResult.ret(RestResult.CODE_CAN_NOT_FIND, RestResult.CAN_NOT_FIND_ITEM);
        }
        AuthParam authParam = new AuthParam();
        authParam.setUserName(req.getUserName());
        authParam.setServiceId(req.getServiceId());
        authParam.setUserAgent("data-effect-shower");
        authParam.setExceptAccessToken(req.getAccessToken());
        authParam.setExceptValidTime(req.getValidTime());
        getAccessToken(authParam);
        return RestResult.success("添加access-token成功");
    }

    private String generateVerifyCode(String loginStamp) {
        StringBuilder sb = new StringBuilder(randomStrNum);
        for (int i = 0; i < randomStrNum; i++) {
            sb.append(randomString.charAt(random.nextInt(randomString.length())));
        }
        String verifyCode = sb.toString();
        verifyRedisTemplate.opsForValue().set(redisVerifyCodeKey(loginStamp), verifyCode,
                VERIFY_CODE_TIMEOUT_MINUTE, TimeUnit.MINUTES);
        return verifyCode;
    }

    private boolean checkManageLoginCondition(SysUser sysUser, boolean isManageLogin) {
        if (!isManageLogin) {
            log.debug("[checkManageLoginCondition]no need judge for not manager login.");
            return true;
        }
        List<Integer> roleIdList = getRoleListByUserId(sysUser.getId());
        int superAdmin = sysRoleMapper.isAdmin(roleIdList);
        if (superAdmin == 0) {
            log.info("[checkManageLoginCondition]Can not login for no super manager");
            return false;
        }
        return true;
    }

    private List<Integer> getRoleListByUserId(int userId) {
        QueryWrapper<SysUserAssociateRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.in(SysUserAssociateRole.fUserId, userId)
                .eq(SysUserAssociateRole.fStatus, 1);
        List<SysUserAssociateRole> userAssociateRoleList = sysUserAssociateRoleMapper.selectList(queryWrapper);
        List<Integer> roleIdList = new ArrayList<>();
        for (SysUserAssociateRole sysUserAssociateRole : userAssociateRoleList) {
            roleIdList.add(sysUserAssociateRole.getRoleId());
        }
        return roleIdList;
    }

    private String redisVerifyCodeKey(String key) {
        return "VerifyKey:" + key;
    }

    private String getAccessToken(AuthParam authParams) {
        String token = authParams.getExceptAccessToken();
        if (StringUtils.isEmpty(token)) {
            token = UUID.randomUUID().toString().replaceAll("-", "");
        }
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(SysUser.fName, authParams.getUserName());
        queryWrapper.eq(SysUser.fServiceId, authParams.getServiceId());
        SysUser sysUser = sysUserMapper.selectOne(queryWrapper);
        List<Integer> roleIdList = getRoleListByUserId(sysUser.getId());
        QueryWrapper<SysRole> sysRoleWrapper = new QueryWrapper<>();
        sysRoleWrapper.in(SysRole.fId, roleIdList);
        List<SysRole> sysRoleList = sysRoleMapper.selectList(sysRoleWrapper);
        sysUser.setPassword("");
        TokenInfo ac = new TokenInfo();
        ac.setUserId(sysUser.getId());
        ac.setSysUserInfo(sysUser);
        ac.setSysRoleList(sysRoleList);
        ac.setUserAgent(authParams.getUserAgent());
        if (authParams.getExceptValidTime() != null) {
            ac.setTimeout(authParams.getExceptValidTime());
        } else {
            ac.setTimeout(getTimeoutByUserAgent(authParams.getUserAgent()));
        }
        ac.setOverTime(LocalDateTime.now().plusMinutes(ac.getTimeout()));
        Duration du = Duration.between(LocalDateTime.now(), ac.getOverTime());
        accessTokenStringRT.opsForValue().set(token, gson.toJson(ac), du);
        return token;
    }
    private int getTimeoutByUserAgent(String userAgent) {
        String lowerUserAgent = userAgent.toLowerCase();
        for (String s : phoneString) {
            if (lowerUserAgent.contains(s)) {
                return phoneTimeout;
            }
        }
        return timeout;
    }

    public TokenInfo getTokenInfo(String token) {
        accessTokenStringRT.opsForValue().set(token, "{'userId':'893'}");
        String tokenInfoStr = accessTokenStringRT.opsForValue().get(token);
        if (tokenInfoStr == null) {
            log.error("[getTokenInfo]Fail to get value from redis by {}", token);
            return null;
        }
        TokenInfo tokenInfo = gson.fromJson(tokenInfoStr, TokenInfo.class);
        if (tokenInfo == null) {
            log.error("[getTokenInfo]Fail to decode from string: {}", tokenInfoStr);
            return null;
        }
        return tokenInfo;
    }
}
