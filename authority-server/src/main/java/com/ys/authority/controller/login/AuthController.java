package com.ys.authority.controller.login;

import com.ys.authority.constant.ConstWeb;
import com.ys.authority.controller.login.param.ValidAccessTokenReq;
import com.ys.authority.controller.login.vo.UserVO;
import com.ys.authority.mapper.dao.SysUser;
import com.ys.authority.controller.login.param.AccessReq;
import com.ys.authority.controller.login.param.AuthParam;
import com.ys.authority.controller.login.param.TokenParams;
import com.ys.authority.controller.login.vo.LoginResultVO;
import com.ys.authority.controller.login.vo.TokenCheckResultVO;
import com.ys.authority.service.LoginService;
import com.ys.authority.utils.annotation.LoginAuthIgnore;
import com.ys.common.utils.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

@Slf4j
@Api(tags = "用户登录API")
@RestController
@RequestMapping("/authority/auth")
public class AuthController {

    @Resource
    HttpServletRequest request;

    @Resource
    HttpServletResponse response;

    // =================自动装备对象================
    private final LoginService loginService;

    @Autowired
    public AuthController(LoginService loginService) {
        this.loginService = loginService;
    }

    @ApiOperation(value = "用户登录")
    @PostMapping("/manageLogin")
    @LoginAuthIgnore
    public RestResult<LoginResultVO> manageLogin(@RequestBody @Valid @ApiParam(required = true) AuthParam authParams) {
        authParams.setUserAgent(request.getHeader(ConstWeb.HeadField.userAgent));
        log.info("###[manageLogin]AuthParam:{}", authParams);
        return loginService.login(authParams, true);
    }

    /**
     * 用户登录接口
     * @return 登录结果
     */
    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    @LoginAuthIgnore
    public RestResult<LoginResultVO> login(@RequestBody @Valid @ApiParam(required = true) AuthParam authParams) {
        authParams.setUserAgent(request.getHeader(ConstWeb.HeadField.userAgent));
        log.info("###[login]AuthParam:{}", authParams);
        return loginService.login(authParams, false);
    }

    /**
     * 用户登出接口
     * @return 登出结果
     */
    @ApiOperation(value = "用户登出")
    @PostMapping("/logout")
    public RestResult<String> logout() {
        String accessToken = request.getHeader("access-token");
        log.info("###[logout]access-token: {}", accessToken);
        loginService.logout(accessToken);
        return RestResult.success(RestResult.SUCCESS);
    }

    /**
     * 获取用户验证码
     */
    @ApiOperation(value = "获取验证码")
    @PostMapping("/getVerifyCodeImage")
    @LoginAuthIgnore
    public void getVerifyCodeImage(@RequestBody @Valid @ApiParam(required = true) AuthParam authParams) {
        log.info("###[getVerifyCodeImage]AuthParam:{}", authParams);
        String loginStamp = authParams.getLoginStamp();
        if (loginStamp == null) {
            response.setStatus(RestResult.CODE_PARAM_ERR);
            return;
        }
        loginService.getVerifyCodeImage(loginStamp, response);
    }

    /**
     *
     * @param checkParams 校验参数
     * @return 校验结果
     */
    @ApiOperation(value = "校验用户token")
    @PostMapping("/checkLoginToken")
    @LoginAuthIgnore
    public RestResult<TokenCheckResultVO> checkLoginToken(@RequestBody @Valid @ApiParam(required = true)
                                                                  TokenParams checkParams) {
        log.info("###[checkLoginToken]TokenParams:{}", checkParams);
        SysUser sysUser = loginService.validateToken(checkParams.getToken());
        TokenCheckResultVO resultVO = new TokenCheckResultVO();
        if (sysUser == null) {
            return resultVO.noAuth();
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(sysUser, userVO);
        userVO.setPassword("");
        resultVO.setUser(userVO);
        return resultVO.success();
    }

    @ApiOperation(value = "校验用户访问资格, 包括 access_token 和 url")
    @PostMapping("/checkAccess")
    @LoginAuthIgnore
    public RestResult<Map<String, Object>> checkAccess(@RequestBody AccessReq req) {
        log.info("###[checkAccess]TokenParams:{}", req);
        try {
            return loginService.checkAccessToken(req);
        } catch (Exception e) {
            return RestResult.ret(RestResult.CODE_INNER_ERROR, RestResult.INNER_ERROR);
        }
    }

    @ApiOperation(value = "动态生效指定用户的access-token，用于内部服务动态指定可用access-token")
    @PostMapping("/validAccessToken")
    @LoginAuthIgnore
    public RestResult<String> validAccessToken(@RequestBody ValidAccessTokenReq req) {
        log.info("###[validAccessToken]Params:{}", req);
        return loginService.validAccessToken(req);
    }

    @ApiOperation(value = "动态生效指定用户的access-token，用于内部服务动态指定可用access-token")
    @PostMapping("/getUserList")
    @LoginAuthIgnore
    public String getUserList(@RequestBody @Valid @ApiParam(required = true) AuthParam authParams) {
        log.info("###[validAccessToken]Params:{}", authParams.getUserName());
        return authParams.getPassword();
    }
}
