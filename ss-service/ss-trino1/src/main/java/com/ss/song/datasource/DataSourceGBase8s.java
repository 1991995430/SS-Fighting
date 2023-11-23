package com.ss.song.datasource;

import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * jdbc:gbasedbt-sqli://%s:%s/%s:GBASEDBTSERVER=%s;SQLMODE=GBase;DB_LOCALE=zh_CN.57372;  IP PORT DBNAME InstanceName
 */
@Slf4j
public class DataSourceGBase8s extends DataSourceRelation {
    public static final String TYPE = "GBase8s".toLowerCase();

    public static final String DATATYPE_VARCHAR = "varchar";
    public static final String DATATYPE_TINYINT = "tinyint";
    public static final String DATATYPE_BIGINT = "bigint";
    public static final String DATATYPE_DECIMAL = "decimal";
    public static final String DATATYPE_DATETIME = "datetime";
    public static final String DATATYPE_TINYTEXT = "tinytext";
    public static final String DATATYPE_LONGTEXT = "longtext";
    public static final String DATATYPE_MEDIUMTEXT = "mediumtext";
    public static final String DATATYPE_DATE = "date";
    public static final String DATATYPE_TEXT = "text";

    protected String instanceName;

    public DataSourceGBase8s(DataSourceParam param) {
        super(param);
        this.driverName = "com.gbasedbt.jdbc.Driver";
        this.instanceName = param.getInstanceName();
    }

    /**
     * 获取 GBase8s 的创建表Sql语句（数据库的分区逻辑没有完全搞懂，先不写）
     * @param tableName 表名
     * @param fields    字段列表
     * @param extendSql 扩展信息，主要是分区的逻辑
     * @return 创建表的Sql语句
     */
    @Override
    protected String getCreateTableSql(String tableName, List<Field> fields, String extendSql) {
        String fieldsSql = buildFieldsSql(fields);
        return String.format("CREATE TABLE '%s' (%s) ENGINE=GsDB DEFAULT CHARSET=utf8 %s",
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
     * 获取 GBase8s 删除表Sql语句
     * @param tableName 表名
     * @return 删除表的Sql语句
     */
    @Override
    protected String getDropTableSql(String tableName) {
        return String.format("DROP TABLE `%s`", tableName);
    }

    private static final Pattern p = Pattern.compile("^jdbc:gbasedbt-sqli://(.*):(\\d+)/(.*):(.*)=(.*)$");
    @Override
    public void analyseConnectionString(DataSourceParam dataSourceParam) {
//        int i = dataSourceParam.getUrl().lastIndexOf(":");
//        Matcher m = p.matcher(dataSourceParam.getUrl().substring(0,i+1));
        Matcher m = p.matcher(dataSourceParam.getUrl().split(";")[0]);
        if (m.find()) {
            dataSourceParam.setIp(m.group(1));
            dataSourceParam.setPort(m.group(2));
            dataSourceParam.setDbName(m.group(3));
            dataSourceParam.setInstanceName(m.group(5));

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
        return String.format("select count(*) cnt, max(%s) ma, min(%s) mi from %s", field, field, tableName);
    }

    @Override
    public String getSourceType() {
        return TYPE;
    }
}
