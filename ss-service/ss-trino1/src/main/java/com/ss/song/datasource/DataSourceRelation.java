package com.ss.song.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.sql.*;
import java.util.List;

/**
 * 关系型数据库数据源基类
 */
@Slf4j
public abstract class DataSourceRelation implements DataSource {
    protected String driverName;

    protected String url;
    protected String userName;
    protected String password;

    protected Connection connection;

    public DataSourceRelation(DataSourceParam param) {
        this.url = param.getUrl();
        this.userName = param.getUserName();
        this.password = param.getPassword();
        this.driverName = "com.mysql.cj.jdbc.Driver";
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
            //log.error("[connect]Fail to get connection for url: {}, error info: {}", url, e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
           // log.error("[close]Close connection failed: {}", e.getMessage());
        }
    }

    /**
     * 支持关系型数据库的查询语句
     * @param sql SQL语句
     * @return 查询到的结果集
     */
    @Override
    public RecordSet query(String sql) {
        RecordSet recordSet = new RecordSet();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            recordSet.setPreparedStatement(preparedStatement);
        } catch (SQLException e){
            //log.error("[query]Prepare statement failed: {}", sql);
            return null;
        }
        try {
            ResultSet resultSet = recordSet.getPreparedStatement().executeQuery();
            recordSet.setResultSet(resultSet);
        } catch (SQLException e) {
            recordSet.close();
           // log.error("[query]Prepare record set failed: {}", sql);
            return null;
        }
        return recordSet;
    }

    /**
     * 支持关系型数据库的更新操作，包括记录的更新和表的create、drop操作
     * @param sql SQL语句
     * @return 操作涉及到的记录数
     */
    @Override
    public int update(String sql) {
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            return preparedStatement.executeUpdate();
        } catch (SQLException e){
           //log.error("[update]Update failed: {}", sql);
            return 0;
        }
    }

    /**
     * 开始事务处理
     */
    @Override
    public boolean startTransaction() {
        try {
            connection.setAutoCommit(false);
            return true;
        } catch (SQLException e) {
            //log.error("[startTransaction]Set auto commit false failed.");
            return false;
        }
    }

    @Override
    public boolean commit() {
        try {
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int insert(String tableName, Record record) {
        String insertSql = buildInsertSql(tableName, record);
        return update(insertSql);
    }

    @Override
    public boolean createTable(String tableName, List<Field> fields, String extendSql) {
        String createSql = getCreateTableSql(tableName, fields, extendSql);
        try(PreparedStatement preparedStatement = connection.prepareStatement(createSql)) {
            preparedStatement.execute();
            //log.info("[createTable]Success to execute sql: {}", createSql);
            return true;
        } catch (SQLException e){
            //log.error("[createTable]Update failed: {}", createSql);
            return false;
        }
    }

    /**
     * 获取创建数据表的sql语句，不同的数据库，语句不同
     * @param tableName 表名
     * @param fields    字段集
     * @param extendSql 扩展sql，暂时没确定怎么用，可能会改接口
     * @return 创建表的sql语句
     */
    protected abstract String getCreateTableSql(String tableName, List<Field> fields, String extendSql);

    /**
     * 创建表用到的字段部分的sql语句，关系型数据库这部分长的都一样
     * @param fields 字段集
     * @return 创建表sql语句中的字段定义部分
     */
    protected String buildFieldsSql(List<Field> fields) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.size(); i++) {
            if (i != 0) {
                sb.append(", ");
            }
            Field field = fields.get(i);
            sb.append(buildCreateFieldString(field));
        }
        String primaryKeySql = buildPrimaryKeySql(fields);
        if (primaryKeySql != null) {
            sb.append(", ");
            sb.append(primaryKeySql);
        }
        return sb.toString();
    }

    /**
     * 创建表sql语句中的主键部分语句
     * @param fields 字段集，使用其中的isPrimaryKey字段
     * @return 创建表sql语句中字段集中的主键部分
     */
    protected String buildPrimaryKeySql(List<Field> fields) {
        int keyNum = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("PRIMARY KEY (");
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            if (!field.isPrimaryKey) {
                continue;
            }
            keyNum++;
            if (i != 0) {
                sb.append(", ");
            }
            sb.append("'");
            sb.append(fields.get(i).getName());
            sb.append("'");
        }
        sb.append(") USING BTREE");
        return keyNum == 0 ? null : sb.toString();
    }

    /**
     * 构建创建/修改表定义中，一个字段定义的sql语句
     * 大部分关系型数据库长的一样，Hive等少部分数据库不同
     * @param field 字段
     * @return 一个字段定义的sql语句
     */
    protected abstract String buildCreateFieldString(Field field);

    @Override
    public void dropTable(String tableName) {
        String deleteSql = getDropTableSql(tableName);
        try(PreparedStatement preparedStatement = connection.prepareStatement(deleteSql)) {
            preparedStatement.execute();
        } catch (SQLException e){
            //log.error("[update]Update failed: {}", deleteSql);
        }
    }

    protected abstract String getDropTableSql(String tableName);

    private String buildInsertSql(String tableName, Record record) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ")
                .append(tableName)
                .append(" VALUES (");
        List<Field> fieldList = record.getFieldList();
        for (int i = 0; i < fieldList.size(); i++) {
            Field f = fieldList.get(i);
            if (i != 0) {
                sb.append(",");
            }
            sb.append("'");
            sb.append(f.getValue().toString());
            sb.append("'");
        }
        sb.append(");");
        return sb.toString();
    }

    protected void gBaseBuildFieldInfo(Field field, StringBuilder builder) {
        if (field.getTypeName() == null) {
            field.setTypeName(DataSourceGBase.DATATYPE_VARCHAR);
        }
        String fieldTypeName = field.getTypeName().toLowerCase();
        if (fieldTypeName.equals(DataSourceGBase.DATATYPE_DECIMAL) && field.getAccuracy() != null) {
            Long accuracy = calcValidAccuracy(field);
            builder.append(String.format(" %s %s(%d,%d) ",
                    field.getName().trim(), field.getTypeName(), field.getLength(), accuracy));
            return;
        }
        if (fieldTypeName.equals(DataSourceGBase.DATATYPE_VARCHAR)) {
            builder.append(String.format(" %s %s(%d) ",
                    field.getName().trim(), field.getTypeName(), field.getLength()));
            return;
        }
        builder.append(String.format(" %s %s", field.getName().trim(), field.getTypeName()));
    }

    protected Long calcValidAccuracy(Field field) {
        Long accuracy = field.getAccuracy();
        if (accuracy < 0L) {
            accuracy = 0L;
        }
        if (accuracy > field.getLength()) {
            accuracy = field.getLength();
        }
        return accuracy;
    }


    protected void gBaseBuildDefaultValue(Field field, StringBuilder builder) {
        String fieldTypeName = field.getTypeName().toLowerCase();
        if (!StringUtils.isEmpty(field.getDefaultValue())) {
            return;
        }
        if (fieldTypeName.equals(DataSourceGBase.DATATYPE_VARCHAR)) {
            builder.append(String.format(" DEFAULT '%s'", field.getDefaultValue()));
            return;
        }
        if (fieldTypeName.equals(DataSourceGBase.DATATYPE_DATETIME)) {
            builder.append(" ON UPDATE CURRENT_TIMESTAMP(5)");
        }
    }

    protected void oracleBuildFieldInfo(Field field, StringBuilder builder) {
        if (field.getTypeName() == null) {
            field.setTypeName(DataSourceOracle.DATATYPE_VARCHAR);
        }
        String typeName = field.getTypeName().toLowerCase();
        if (DataSourceOracle.DATATYPE_VARCHAR.equals(typeName) || DataSourceOracle.DATATYPE_VARCHAR2.equals(typeName)) {
            builder.append(String.format(" \"%s\" %s(%d) ", field.getName().trim(), typeName,
                    field.getLength() < 2 ? 10L : field.getLength()));
            return;
        }
        if (DataSourceOracle.DATATYPE_NUMBER.equals(typeName) || DataSourceOracle.DATATYPE_FLOAT.equals(typeName)) {
            builder.append(String.format(" \"%s\" %s(%d) ", field.getName().trim(), typeName, field.getLength()));
            return;
        }
        builder.append(String.format(" \"%s\" %s ", field.getName().trim(), typeName));
    }

    protected void oracleBuildDefaultValue(Field field, StringBuilder builder) {
        if (field.getDefaultValue() == null) {
            return;
        }
        String typeName = field.getTypeName().toLowerCase();
        switch (typeName) {
            case DataSourceOracle.DATATYPE_VARCHAR2: {
                builder.append(String.format(" DEFAULT '%s'", field.getDefaultValue()));
                break;
            }
            case DataSourceOracle.DATATYPE_NUMBER: {
                builder.append(String.format(" DEFAULT %s", field.getDefaultValue()));
                break;
            }
            case DataSourceOracle.DATATYPE_DATE: {
                builder.append(" DEFAULT to_date(to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss')");
                break;
            }
            default: {
            }
        }
    }

    @Override
    public boolean mkdir(String path) {
        assert (false);
        return false;
    }

    @Override
    public int dus() {
        return 0;
    }

    @Override
    public int dusWithNameSpace() {
        return 0;
    }

    @Override
    public void write(String fileName, byte[] data) {}

    @Override
    public void copyToLocalFile(String remotePath, String localPath) {}

    @Override
    public boolean isExist(String fileName) {
        return false;
    }

    @Override
    public int preview() {
        return 0;
    }

    @Override
    public void delete(String path) {
        assert (false);
    }

    @Override
    public void copyFromLocalFile(String localPath, String remotePath) {}

    @Override
    public Connection getConnection() {
        return this.connection;
    }

    /*@Override
    public FileSystem getFileSystem() {
        return null;
    }

    @Override
    public MongoClient getMongoClient() {
        return null;
    }

    @Override
    public MongoDatabase getMongoDatabase() {
        return null;
    }*/

}
