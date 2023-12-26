package com.ss.song.dto;

import java.util.List;

/**
 * author shangsong 2023/4/3
 */

public class QueryResultVo {

    private String queryId;

    private String queryStatus;

    private List<Object> queryResult;

    private List<String> columns;

    private String infoUri;

    private String statsInfo;
    private String errorInfo;

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getQueryStatus() {
        return queryStatus;
    }

    public void setQueryStatus(String queryStatus) {
        this.queryStatus = queryStatus;
    }

    public List<Object> getQueryResult() {
        return queryResult;
    }

    public void setQueryResult(List<Object> queryResult) {
        this.queryResult = queryResult;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public String getInfoUri() {
        return infoUri;
    }

    public void setInfoUri(String infoUri) {
        this.infoUri = infoUri;
    }

    public String getStatsInfo() {
        return statsInfo;
    }

    public void setStatsInfo(String statsInfo) {
        this.statsInfo = statsInfo;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }
}
