package com.ss.song.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * author shangsong 2023/12/22
 */
@Service
public class JdbcService {
    @Resource
    private JdbcTemplate jdbcTemplate;

    private void query() {

    }
}
