package com.ss.song.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * author shangsong 2023/7/18
 */
public class Test0718001 {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        String conn = "connector.name=sqlserver\nconnection-url=jdbc:sqlserver://192.168.6.251:1433;databaseName=datastudio_source;encrypt=false\nconnection-user=sa\nconnection-password=Root$123";
        System.out.println(getParamMap(conn));

    }

    private static Map<String, String> getParamMap(String connectionParam) {
        String[] params = connectionParam.split("\n");
        Map<String, String> paramMap = new HashMap<>();
        for (String param : params) {
            String[] split = param.split("=", 2);
            if (split[0].equals("connector.name")) {
                continue;
            }
            try {
                paramMap.put(split[0], split[1]);
            } catch (Exception e) {
            }
        }
        return paramMap;
    }

}
