package com.ss.song.datasource;

import java.io.Closeable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * jdbc:kingbase://%s:%s/%s  IP PORT DBNAME
 */
public class DataSourceKingBase extends DataSourceRelation {
    public static final String TYPE = "KingBase".toLowerCase();

    public static final String DATATYPE_VARCHAR2 = "varchar2";
    public static final String DATATYPE_VARCHAR = "varchar";
    public static final String DATATYPE_NUMBER = "number";
    public static final String DATATYPE_FLOAT = "float";
    public static final String DATATYPE_DATE = "date";
    public static final String DATATYPE_DATETIME = "datetime";
    public static final String DATATYPE_CLOB = "clob";
    public static final String DATATYPE_BLOB = "blob";

    public DataSourceKingBase(DataSourceParam param) {
        super(param);
        this.driverName = "com.kingbase8.Driver";
    }

    /**
     * 获取 KingBase 的创建表Sql语句(与Oracle一直)
     * @param tableName 表名
     * @param fields    字段列表
     * @param extendSql 扩展信息
     * @return 创建表的Sql语句
     */
    @Override
    protected String getCreateTableSql(String tableName, List<Field> fields, String extendSql) {
        String fieldsSql = buildFieldsSql(fields);
        return String.format("CREATE TABLE \"%s\" (%s) %s", tableName, fieldsSql, extendSql);
    }

    @Override
    protected String buildCreateFieldString(Field field) {
        StringBuilder sb = new StringBuilder();
        oracleBuildFieldInfo(field, sb);
        oracleBuildDefaultValue(field, sb);
        sb.append(field.getAllowNull() ? "" : " NOT NULL");
        return sb.toString();
    }

    /**
     * 获取 KingBase 删除表Sql语句
     * @param tableName 表名
     * @return 删除表的Sql语句
     */
    @Override
    protected String getDropTableSql(String tableName) {
        return String.format("DROP TABLE \"%s\"", tableName);
    }

    private static final Pattern p = Pattern.compile("^jdbc:kingbase8://(.*):(.*)/(.*)");
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
        return String.format("select * from \"%s\" limit %d", tableName, limit);
    }

    @Override
    public String getCountSql(String tableName, String field) {
        return String.format("select count(*) cnt, max(%s) ma, min(%s) mi from %s", field, field, tableName);
    }

    @Override
    public String getSourceType() {
        return TYPE;
    }
}
