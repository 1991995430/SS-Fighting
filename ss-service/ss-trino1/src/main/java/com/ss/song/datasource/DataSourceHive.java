package com.ss.song.datasource;

import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * jdbc:hive2://%s:%s/%s  IP PORT DBNAME
 */
@Slf4j
public class DataSourceHive extends DataSourceRelation {
    public static final String TYPE = "Hive".toLowerCase();
    protected DataSourceParam proxy;

    public DataSourceHive(DataSourceParam param) {
        super(param);
        this.driverName = "org.apache.hive.jdbc.HiveDriver";
        proxy = new DataSourceParam();
        this.proxy = param.getProxy();
    }

    /**
     * 获取 Hive 的创建表Sql语句
     *
     * @param tableName 表名
     * @param fields    字段列表
     * @param extendSql 扩展信息, 本数据库不涉及
     * @return 创建表的Sql语句
     */
    @Override
    protected String getCreateTableSql(String tableName, List<Field> fields, String extendSql) {
        String fieldsSql = buildFieldsSql(fields);
        return String.format("CREATE %s (%s) %s", tableName, fieldsSql, extendSql);
    }

    /**
     * 获取 Hive 删除表Sql语句
     *
     * @param tableName 表名
     * @return 删除表的Sql语句
     */
    @Override
    protected String getDropTableSql(String tableName) {
        return String.format("DROP TABLE IF EXISTS `%s`", tableName);
    }

    // 参考环境中，Hive的没有实现
    @Override
    protected String buildCreateFieldString(Field field) {
        return String.format("`%s` %s COMMENT '%s'", field.getName(), field.getTypeName(), field.getAliasName());
    }

    private static final Pattern p = Pattern.compile("^jdbc:hive2://(.*):(\\d+)/(.*)$");

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
        return String.format("select * from %s limit %d", tableName, limit);
    }

    @Override
    public String getCountSql(String tableName, String field) {
        return String.format("select count(*) cnt, max(%s) ma, min(%s) mi from %s", field, field, tableName);
    }

    @Override
    public boolean connect() {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            //log.error("[connect]Can not find class: {}", driverName);
            return false;
        }
        try {
            connection = DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
            //log.error("[connect]Fail to get connection for url: {}", url);
            return false;
        }
        DataSourceMySql proxyMysql = new DataSourceMySql(proxy);
        return proxyMysql.connect();

    }

    @Override
    public String getSourceType() {
        return TYPE;
    }
}
