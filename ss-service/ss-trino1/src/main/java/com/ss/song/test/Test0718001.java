package com.ss.song.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * author shangsong 2023/7/18
 */
public class Test0718001 {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        Class.forName("com.gbase.jdbc.Driver");

        // 创建数据库连接
        String url = "jdbc:gbase://192.168.3.208:5258/wltest";
        String username = "root";
        String password = "root";
        Connection conn = DriverManager.getConnection(url, username, password);

        ResultSet resultSet1 = conn.getMetaData().getSchemas();

        while (resultSet1.next()) {
            System.out.println(resultSet1.getString("TABLE_SCHEMA"));
        }

    }

}
