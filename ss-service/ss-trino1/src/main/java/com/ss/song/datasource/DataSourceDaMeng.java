package com.ss.song.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.Closeable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * jdbc:DM://%s:%s/%s   IP PORT DBNAME
 */
@Slf4j
public class DataSourceDaMeng extends DataSourceRelation {
    public static final String TYPE = "DaMeng".toLowerCase();

    public static final String DATATYPE_VARCHAR = "varchar";
    public static final String DATATYPE_TINYINT = "tinyint";
    public static final String DATATYPE_BIGINT = "bigint";
    public static final String DATATYPE_INTEGER = "integer";
    public static final String DATATYPE_DECIMAL = "decimal";
    public static final String DATATYPE_DATETIME = "datetime";
    public static final String DATATYPE_DATE = "date";
    public static final String DATATYPE_TEXT = "text";

    protected String dbName = "";

    public DataSourceDaMeng(DataSourceParam param) {
        super(param);
        this.driverName = "dm.jdbc.driver.DmDriver";

        Pattern pattern = Pattern.compile("^jdbc:DM://(.*):(.*)/(.*)?.*");    // "jdbc:DM://%s:%s/%s?"
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            dbName = matcher.group(3);
        }
    }

    /**
     * 获取 达梦 的创建表Sql语句
     *
     * @param tableName 表名
     * @param fields    字段列表
     * @param extendSql 扩展信息，主要是分区的逻辑
     * @return 创建表的Sql语句
     */
    @Override
    protected String getCreateTableSql(String tableName, List<Field> fields, String extendSql) {
        String fieldsSql = buildFieldsSql(fields);
        return String.format("CREATE TABLE \"%s\".\"%s\" (%s) %s", dbName, tableName, fieldsSql, extendSql);
    }

    @Override
    protected String buildCreateFieldString(Field field) {
        StringBuilder sb = new StringBuilder();
        buildFieldInfo(field, sb);
        buildDefaultValue(field, sb);
        sb.append(field.getAllowNull() ? "" : " NOT NULL");
        return sb.toString();
    }

    /**
     * 获取 达梦 删除表Sql语句
     *
     * @param tableName 表名
     * @return 删除表的Sql语句
     */
    @Override
    protected String getDropTableSql(String tableName) {
        return String.format("DROP TABLE \"%s\".\"%s\"", dbName, tableName);
    }

    private void buildFieldInfo(Field field, StringBuilder builder) {
        if (field.getTypeName() == null) {
            field.setTypeName(DATATYPE_VARCHAR);
        }
        String fieldTypeName = field.getTypeName().toLowerCase();
        if (fieldTypeName.equals(DATATYPE_DECIMAL) && field.getAccuracy() != null) {
            Long accuracy = calcValidAccuracy(field);
            builder.append(String.format(" \"%s\" %s(%d,%d) ",
                    field.getName().trim(), field.getTypeName(), field.getLength(), accuracy));
            return;
        }
        if (fieldTypeName.equals(DATATYPE_VARCHAR)) {
            builder.append(String.format(" \"%s\" %s(%d) ",
                    field.getName().trim(), field.getTypeName(), field.getLength()));
            return;
        }
        builder.append(String.format(" \"%s\" %s", field.getName().trim(), field.getTypeName()));
    }

    private void buildDefaultValue(Field field, StringBuilder builder) {
        String fieldTypeName = field.getTypeName().toLowerCase();
        if (!StringUtils.isEmpty(field.getDefaultValue())) {
            return;
        }
        if (fieldTypeName.equals(DATATYPE_VARCHAR)) {
            builder.append(String.format(" DEFAULT '%s'", field.getDefaultValue()));
        }
    }

    private static final Pattern p = Pattern.compile("^jdbc:dm://(.*):(.*)/(.*)");

    @Override
    public void analyseConnectionString(DataSourceParam dataSourceParam) {
        Matcher m = p.matcher(dataSourceParam.getUrl());
        if (m.find()) {
            dataSourceParam.setIp(m.group(1));
            dataSourceParam.setPort(m.group(2));
            dataSourceParam.setDbName(m.group(3));
            dbName = m.group(3);
        }
    }

    @Override
    public Closeable getFileSystem() {
        return null;
    }

    @Override
    public String getPreviewSql(String tableName, Integer limit) {
        return String.format("select * from %s limit %d", tableName, limit);
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
