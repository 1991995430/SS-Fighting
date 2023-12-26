package com.ss.song.test;

import com.ss.song.model.JdbcResource;

import java.util.HashMap;
import java.util.Map;

/**
 * author shangsong 2023/12/26
 */
public class Test1226 {
    public static void main(String[] args) {

        Map<Integer, Map<String, Object>> cacheData = new HashMap<>();
        JdbcResource jdbcResource = new JdbcResource();
        jdbcResource.setName("ss");
        jdbcResource.setId("12333");
        Map<String, Object> data = new HashMap<>();
        data.put("ss", jdbcResource);
        cacheData.put(1, data);
        Map<String, Object> stringObjectMap = cacheData.get(1);

        JdbcResource jdbcResource1 = new JdbcResource();
        jdbcResource1.setName("ss111");
        jdbcResource1.setId("666");
        stringObjectMap.put("sss", jdbcResource1);

        System.out.println();
    }
}
