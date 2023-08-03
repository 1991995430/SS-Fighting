package com.ys.authority.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * 处理请求工具类
 *
 * @author suwenbao
 * @since 2022/8/8
 */
public class RequestUtil {
    private final static String ADDR_HEADER = "X-Forwarded-For";

    public static String getIpAddr(HttpServletRequest httpServletRequest) {
        String header = httpServletRequest.getHeader(ADDR_HEADER);
        return header != null ? header : httpServletRequest.getRemoteAddr();
    }
}
