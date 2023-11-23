package com.ss.song.datasource;

import java.io.Closeable;
import java.util.List;

/**
 * jdbc:informix-sqli://%s:%s/%s:informixserver=%s;%s        IP PORT DBNAME InformixServer  GBASE_LANG
 * GBASE_LANG:DB_LOCALE=zh_cn.GB18030-2000;CLIENT_LOCALE=zh_cn.GB18030-2000;NEWCODESET=GB18030,GB18030-2000,5488;DBDATE=Y4MD
 */
public class DataSourceGBase extends DataSourceRelation {
    public static final String TYPE = "GBase".toLowerCase();

    public static final String DATATYPE_VARCHAR = "varchar";
    public static final String DATATYPE_TINYINT = "tinyint";
    public static final String DATATYPE_BIGINT = "bigint";
    public static final String DATATYPE_INTEGER = "integer";
    public static final String DATATYPE_DECIMAL = "decimal";
    public static final String DATATYPE_DATETIME = "datetime";
    public static final String DATATYPE_DATE = "date";
    public static final String DATATYPE_TEXT = "text";

    public DataSourceGBase(DataSourceParam param) {
        super(param);
        this.driverName = "com.informix.jdbc.IfxDriver";
    }

    /**
     * 获取 GBase 的创建表Sql语句
     * @param tableName 表名
     * @param fields    字段列表
     * @param extendSql 扩展信息, 本数据库不涉及
     * @return 创建表的Sql语句
     */
    @Override
    protected String getCreateTableSql(String tableName, List<Field> fields, String extendSql) {
        String fieldsSql = buildFieldsSql(fields);
        return String.format("CREATE TABLE '%s' (%s)", tableName, fieldsSql);
    }

    @Override
    protected String buildCreateFieldString(Field field) {
        StringBuilder sb = new StringBuilder();
        gBaseBuildFieldInfo(field, sb);
        gBaseBuildDefaultValue(field, sb);
        sb.append(field.getAllowNull() ? "" : " NOT NULL");
        return sb.toString();
    }

    /**
     * 获取 GBase 删除表Sql语句
     * @param tableName 表名
     * @return 删除表的Sql语句
     */
    @Override
    protected String getDropTableSql(String tableName) {
        return String.format("DROP TABLE IF EXISTS %s", tableName);
    }

    @Override
    public void analyseConnectionString(DataSourceParam dataSourceParam) {

    }

    @Override
    public Closeable getFileSystem() {
        return null;
    }

    @Override
    public String getPreviewSql(String tableName, Integer limit) {
        return String.format("select * from %s where rownum <= %d", tableName, limit);
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
