package com.ss.song.datasource;

import java.io.Closeable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * jdbc:postgresql://[ip]:[port]/[database]
 */
public class DataSourcePostgresql extends DataSourceRelation {
    public static final String TYPE = "Postgresql".toLowerCase();
    public DataSourcePostgresql(DataSourceParam param) {
        super(param);
        this.driverName = "org.postgresql.Driver";
    }

    @Override
    protected String getCreateTableSql(String tableName, List<Field> fields, String extendSql) {
        return null;
    }

    @Override
    protected String buildCreateFieldString(Field field) {
        return null;
    }

    @Override
    protected String getDropTableSql(String tableName) {
        return null;
    }

    private static final Pattern p = Pattern.compile("^jdbc:postgresql://(.*):(\\d+)/(.*)$");
    @Override
    public void analyseConnectionString(DataSourceParam dataSourceParam) {
        Matcher m = p.matcher(dataSourceParam.getUrl());
        if (m.find()) {
            dataSourceParam.setIp(m.group(1));
            dataSourceParam.setPort(m.group(2));
            dataSourceParam.setDbName(m.group(3));
        }
    }

    @Override
    public Closeable getFileSystem() {
        return null;
    }

    @Override
    public String getPreviewSql(String tableName, Integer limit) {
        return null;
    }

    @Override
    public String getCountSql(String tableName, String field) {
        return null;
    }

    @Override
    public String getSourceType() {
        return TYPE;
    }
}
