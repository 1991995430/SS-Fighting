package com.ss.song.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Test1558 {

    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //与数据库建立连接
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/shangsong","root","199551");
            for (int i = 200001; i < 210000; i++) {
                ps = conn.prepareStatement("insert into user values (?,?,?,?)");
                ps.setInt(1, i);
                ps.setString(2, "ss" + i);
                ps.setInt(3, 19);
                ps.setInt(4, 1);
                ps.execute();
            }


        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }


    }

}
