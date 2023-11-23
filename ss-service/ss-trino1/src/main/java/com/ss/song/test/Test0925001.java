package com.ss.song.test;

import com.ss.song.dto.DataDto;
import org.apache.commons.lang.StringEscapeUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * author shangsong 2023/9/25
 */
public class Test0925001 {

    public static void main(String[] args) {
        String username = "admin";
        String password = "admin";
        String sec = "Basic YWRtaW46YWRtaW4=";
        String credentials = username + ":" + password;
        System.out.println(Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8)));
        System.out.println(Base64.getUrlDecoder().decode(sec.substring(6)));
    }

}
