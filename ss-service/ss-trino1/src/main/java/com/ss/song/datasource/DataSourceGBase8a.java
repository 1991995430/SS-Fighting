package com.ss.song.datasource;

import java.io.Closeable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * jdbc:gbase://%s:%s/%s?useUnicode=true&characterEncoding=utf8&rewriteBatchedStatements=true   IP PORT DBNAME
 */
public class DataSourceGBase8a extends DataSourceRelation {
    public static final String TYPE = "GBase8a".toLowerCase();

    public static final String DATATYPE_VARCHAR = "varchar";
    public static final String DATATYPE_TINYINT = "tinyint";
    public static final String DATATYPE_BIGINT = "bigint";
    public static final String DATATYPE_DECIMAL = "decimal";
    public static final String DATATYPE_DATETIME = "datetime";
    public static final String DATATYPE_DATE = "date";
    public static final String DATATYPE_TEXT = "text";

    public DataSourceGBase8a(DataSourceParam param) {
        super(param);
        this.driverName = "com.gbase.jdbc.Driver";
    }

    /**
     * 获取 GBase8a 的创建表Sql语句（数据库的分区逻辑没有完全搞懂，先不写）
     * @param tableName 表名
     * @param fields    字段列表
     * @param extendSql 扩展信息，主要是分区的逻辑
     * @return 创建表的Sql语句
     */
    @Override
    protected String getCreateTableSql(String tableName, List<Field> fields, String extendSql) {
        String fieldsSql = buildFieldsSql(fields);
        return String.format("CREATE TABLE '%s' (%s) ENGINE=EXPRESS DEFAULT CHARSET=utf8 %s",
                tableName, fieldsSql, extendSql);
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
     * 获取 GBase8a 删除表Sql语句
     * @param tableName 表名
     * @return 删除表的Sql语句
     */
    @Override
    protected String getDropTableSql(String tableName) {
        return String.format("DROP TABLE `%s`", tableName);
    }

    private static final Pattern p = Pattern.compile("^jdbc:gbase://(.*):(\\d+)/(.*)?$");
    @Override
    public void analyseConnectionString(DataSourceParam dataSourceParam) {
        Matcher m = p.matcher(dataSourceParam.getUrl().split("\\?")[0]);
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
        return String.format("select * from `%s` limit %d", tableName, limit);
    }

    @Override
    public String getCountSql(String tableName, String field) {
        return String.format("select count(*) cnt,max(%s) ma,min(%s) mi from %s", field, field, tableName);
    }

    @Override
    public String getSourceType() {
        return TYPE;
    }
}
