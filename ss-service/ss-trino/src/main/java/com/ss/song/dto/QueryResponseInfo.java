package com.ss.song.dto;

import java.util.List;

/**
 * author shangsong 2023/4/23
 */
public class QueryResponseInfo {
    private String id;

    private String infoUri;

    private String partialCancelUri;

    private String nextUri;

    private List<Column> columns;

    private List<Object> data;

    private QueryRespStats stats;

    private Object warnings;

    private Object error;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInfoUri() {
        return infoUri;
    }

    public void setInfoUri(String infoUri) {
        this.infoUri = infoUri;
    }

    public String getPartialCancelUri() {
        return partialCancelUri;
    }

    public void setPartialCancelUri(String partialCancelUri) {
        this.partialCancelUri = partialCancelUri;
    }

    public String getNextUri() {
        return nextUri;
    }

    public void setNextUri(String nextUri) {
        this.nextUri = nextUri;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public QueryRespStats getStats() {
        return stats;
    }

    public void setStats(QueryRespStats stats) {
        this.stats = stats;
    }

    public Object getWarnings() {
        return warnings;
    }

    public void setWarnings(Object warnings) {
        this.warnings = warnings;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }
}
