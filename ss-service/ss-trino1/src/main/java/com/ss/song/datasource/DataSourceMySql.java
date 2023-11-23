package com.ss.song.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.Closeable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull IP PORT DBNAME
 */
@Slf4j
public class DataSourceMySql extends DataSourceRelation {
    public static final String TYPE = "MySql".toLowerCase();

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

    public static Map<String, Boolean> noLengthDataTypes = new HashMap<>();
    static {
        noLengthDataTypes.put(DATATYPE_DATE, true);
        noLengthDataTypes.put(DATATYPE_DATETIME, true);
        noLengthDataTypes.put(DATATYPE_TEXT, true);
        noLengthDataTypes.put(DATATYPE_TINYTEXT, true);
        noLengthDataTypes.put(DATATYPE_MEDIUMTEXT, true);
        noLengthDataTypes.put(DATATYPE_LONGTEXT, true);
    }

    public DataSourceMySql(DataSourceParam param) {
        super(param);
        this.driverName = "com.mysql.cj.jdbc.Driver";
    }

    /**
     * 获取 MySql 的创建表Sql语句（数据库的分区逻辑没有完全搞懂，先不写）
     * @param tableName 表名
     * @param fields    字段列表
     * @param extendSql 扩展信息，主要是分区的逻辑
     * @return 创建表的Sql语句
     */
    @Override
    protected String getCreateTableSql(String tableName, List<Field> fields, String extendSql) {
        String fieldsSql = buildFieldsSql(fields);
        return String.format("CREATE TABLE '%s' (%s) ENGINE=InnoDB DEFAULT CHARSET=utf8 %s",
                tableName, fieldsSql, extendSql);
    }

    /**
     * 获取 MySql 删除表Sql语句
     * @param tableName 表名
     * @return 删除表的Sql语句
     */
    @Override
    protected String getDropTableSql(String tableName) {
        return String.format("DROP TABLE IF EXISTS '%s'", tableName);
    }

    /**
     * 构建MySql创建/修改表定义中，一个字段定义的sql语句
     * 大部分关系型数据库长的一样，Hive等少部分数据库不同
     * @param field 字段
     * @return 一个字段定义的sql语句
     */
    @Override
    protected String buildCreateFieldString(Field field) {
        StringBuilder sb = new StringBuilder();
        buildFieldInfo(field, sb);
        buildDefaultValue(field, sb);
        sb.append(field.getAllowNull() ? " NULL" : " NOT NULL");
        sb.append(field.getAutoIncrement() ? " AUTO_INCREMENT" : "");
        sb.append(field.getComment() != null ? " COMMENT " + field.getComment() : "");
        return sb.toString();
    }

    private void buildFieldInfo(Field field, StringBuilder builder) {
        if (field.getTypeName() == null) {
            field.setTypeName(DATATYPE_VARCHAR);
        }
        String fieldTypeName = field.getTypeName().toLowerCase();
        if (fieldTypeName.equals(DATATYPE_DECIMAL) && field.getAccuracy() != null) {
            Long accuracy = calcValidAccuracy(field);
            builder.append(String.format(" `%s` %s(%d,%d) ",
                    field.getName().trim(), field.getTypeName(), field.getLength(), accuracy));
            return;
        }
        if (noLengthDataTypes.containsKey(fieldTypeName)) {
            builder.append(String.format(" `%s` %s ",field.getName().trim(), field.getTypeName()));
            return;
        }
        builder.append(String.format(" `%s` %s(%d) ", field.getName().trim(), field.getTypeName(),
                field.getLength() >= 255 ? 255 : field.getLength()));
    }

    private void buildDefaultValue(Field field, StringBuilder builder) {
        if (!StringUtils.isEmpty(field.getDefaultValue())) {
            return;
        }
        String typeName = field.getTypeName();
        if (typeName.equalsIgnoreCase(DATATYPE_VARCHAR)) {
            builder.append(String.format(" DEFAULT '%s'", field.getDefaultValue()));
        }
        if (typeName.equalsIgnoreCase(DATATYPE_DATETIME)) {
            builder.append(" ON UPDATE CURRENT_TIMESTAMP(5)");
        }
    }

    private static final Pattern p = Pattern.compile("^jdbc:mysql://(.*)/(.*)?$");

    @Override
    public String getSourceType() {
        return TYPE;
    }

    @Override
    public void analyseConnectionString(DataSourceParam dataSourceParam) {
        Matcher m = p.matcher(dataSourceParam.getUrl().split("\\?")[0]);
        if (m.find()) {
            String s = m.group(1);
            if(s.contains(":")){
                String[] a = s.split(":");
                dataSourceParam.setIp(a[0]);
                dataSourceParam.setPort(a[1]);
            }else {
                dataSourceParam.setIp(s);
                dataSourceParam.setPort("3306");
            }
            dataSourceParam.setDbName(m.group(2));
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
}
