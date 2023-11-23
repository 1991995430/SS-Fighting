package com.ss.song.test;

/**
 * author shangsong 2023/7/18
 */
public class Test0718 {

    public static void main(String[] args) {
        String regx = "^jdbc:[a-z0-9\\-]+:(?s:.*)$";
        String url1 = "jdbc:gbasedbt-sqli://192.168.3.208:19088/ssetest";
        String url2 = "jdbc:gbase://192.168.3.208:5258/wltest";
        System.out.println(url1.matches(regx));
        System.out.println(url2.matches(regx));

        String ss = "thrift://192.168.3.201:9083";
        System.out.println(ss.substring(ss.lastIndexOf("/") + 1, ss.lastIndexOf(":")));
    }
}
