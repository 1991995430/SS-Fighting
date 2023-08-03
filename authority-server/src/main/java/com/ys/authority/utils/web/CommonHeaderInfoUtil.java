package com.ys.authority.utils.web;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ys.authority.constant.ConstWeb;
import com.ys.authority.controller.login.vo.UserVO;
import com.ys.authority.entity.web.CommonHeaderInfo;
import com.ys.authority.mapper.dao.SysRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Header中的通用信息
 *
 * @author liyj1
 */
@Slf4j
public class CommonHeaderInfoUtil {
    private static final Gson gson = new Gson();

    protected static final String H_INFO = "h-info";

    /**
     * 通用信息
     */
    private static ThreadLocal<CommonHeaderInfo> commonHeaderInfoThreadLocal = new ThreadLocal<>();

    /**
     * 初始化通用header信息。
     */
    public static void initInfo() {
        try {
            ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (sra == null) {
                log.error("CommonHeaderInfoUtil.initInfo fail fro sra is null.");
                return;
            }
            HttpServletRequest request = sra.getRequest();

            String commonInfo = request.getHeader(H_INFO);
            CommonHeaderInfo commonHeaderInfo = gson.fromJson(commonInfo, CommonHeaderInfo.class);
            if (commonHeaderInfo == null) {
                commonHeaderInfo = new CommonHeaderInfo();
            }
            if (StringUtils.isEmpty(commonHeaderInfo.getIp())) {
                commonHeaderInfo.setIp(getRemoteAddress(request));
            }
            setInfo(commonHeaderInfo);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置通用信息
     *
     * @param commonHeaderInfo 通用信息
     */
    public static void setInfo(CommonHeaderInfo commonHeaderInfo) {
        commonHeaderInfoThreadLocal.set(commonHeaderInfo);
    }

    /**
     * 获取通用信息
     *
     * @return 通用信息
     */
    public static CommonHeaderInfo getInfo() {
        return commonHeaderInfoThreadLocal.get();
    }

    /**
     * 获取Header中的登录token
     *
     * @return token
     */
    public static String getToken() {
        CommonHeaderInfo commonHeaderInfo = getInfo();
        String token = null;
        if (commonHeaderInfo != null) {
            token = commonHeaderInfo.getTk();
        }
        return token;
    }

    public static UserVO getUserInfoFromHttpRequest(HttpServletRequest httpServletRequest) {
        String userInfoStr = httpServletRequest.getHeader(ConstWeb.userInfo);
        String userRoleStr = httpServletRequest.getHeader(ConstWeb.roleInfoList);
        if (userInfoStr == null) {
            log.error("[getUserInfoFromHttpRequest]Miss user info");
            return null;
        }
        if (userRoleStr == null) {
            log.error("[getRoleInfoFromHttpRequest]Miss Role info");
            return null;
        }
        try {
            String decodeStr = URLDecoder.decode(userInfoStr, "UTF-8");
            UserVO userVO = gson.fromJson(decodeStr, UserVO.class);
            String decodeStrRole = URLDecoder.decode(userRoleStr, "UTF-8");
            log.info("decodeStrRole user info: {}", decodeStrRole);
            List<SysRole> sysRoleList = gson.fromJson(decodeStrRole, new TypeToken<List<SysRole>>() {
            }.getType());
            log.info("[sysRole]Fail to decode user info: {}", sysRoleList);
            if (userVO == null) {
                log.error("[getUserInfoFromHttpRequest]Fail to decode user info");
                return null;
            }
            if (sysRoleList == null) {
                log.error("[getRoleInfoFromHttpRequest]Fail to decode role info");
                return null;
            }
            List<Integer> roleIdList = new ArrayList<>();
            for (SysRole sysRole : sysRoleList
            ) {
                roleIdList.add(sysRole.getId());
            }
            userVO.setRoleIdList(roleIdList);
            return userVO;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取客户端请求IP地址
     *
     * @param request 用户请求
     * @return 地址
     */
    private static String getRemoteAddress(HttpServletRequest request) {
        String[] ipHeaders = {"X-Forwarded-For",    //X-Forwarded-For：Squid 服务代理
                "Proxy-Client-IP",                  //Proxy-Client-IP：apache 服务代理
                "WL-Proxy-Client-IP",               //WL-Proxy-Client-IP：web logic 服务代理
                "HTTP_CLIENT_IP",                   //HTTP_CLIENT_IP：有些代理服务器
                "X-Real-IP"};                       //X-Real-IP：nginx服务代理
        String ipAddresses;
        for (String header : ipHeaders) {
            ipAddresses = request.getHeader(header);
            if (ipAddresses != null && ipAddresses.length() != 0 && !"unknown".equalsIgnoreCase(ipAddresses)) {
                //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
                return ipAddresses.split(",")[0];
            }
        }

        return request.getRemoteAddr() + ":" + request.getRemotePort();
    }
}
