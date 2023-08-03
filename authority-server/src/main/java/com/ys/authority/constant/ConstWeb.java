package com.ys.authority.constant;

public class ConstWeb {
    public interface HeadField {
        String userAgent = "User-Agent";
        String accessToken = "access-token";
    }

    // 网关填写的http头固定字段userInfo
    public static String userInfo = "userInfo";

    public static String roleInfo = "roleInfo";
    public static String roleInfoList = "roleInfoList";

    public static final String ERR_STR_GET_USER = "Fail to get user info";
}
