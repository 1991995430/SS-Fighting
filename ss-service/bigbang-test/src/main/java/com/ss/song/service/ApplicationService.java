package com.ss.song.service;

import com.ss.song.model.JdbcResource;

import java.util.List;

/**
 * author shangsong 2023/12/25
 */
public interface ApplicationService {
    void insertJdbcResource(JdbcResource resource);

    List<JdbcResource> getJdbcResourceList(String queryString);

    void updateJdbcResource(String jdbcResourceId, JdbcResource resource);

    String tread();

    void deleteJdbcResource(String jdbcResourceId);
}
