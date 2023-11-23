package com.ss.song.datasource;

import java.io.Closeable;
import java.sql.Connection;
import java.util.List;

public interface DataSource {

    boolean connect();

    void close();

    boolean startTransaction();

    boolean commit();

    RecordSet query(String sql);

    int update(String sql);

    int insert(String tableName, Record record);

    boolean createTable(String tableName, List<Field> fields, String extendSql);

    void dropTable(String tableName);

    void analyseConnectionString(DataSourceParam dataSourceParam);

    Closeable getFileSystem();

    /**
     * 获取指定数据源的预览数据Sql语句
     * @param tableName 表名
     * @param limit 字段名
     * @return 对应数据源的Sql语句
     */
    String getPreviewSql(String tableName, Integer limit);

    /**
     * 获取指定表中指定字段的记录数、最大值，最小值的SQL语句
     * @param tableName 表名
     * @param field 字段名
     * @return 对应数据源的Sql语句
     */
    String getCountSql(String tableName, String field);

    /**
     * 文件系统类接口
     * 创建目录
     * @param path 目录路径
     * @return 是否创建成功
     */
    boolean mkdir(String path);

    int dus();

    int dusWithNameSpace();

    /**
     * 文件系统接口
     * 写文件, 本接口仅支持在文件尾顺序写入
     * @param fileName 文件名
     * @param data 写文件内容
     */
    void write(String fileName, byte[] data);

    void copyToLocalFile(String remotePath, String localPath);

    boolean isExist(String fileName);

    int preview();

    /**
     * 文件系统接口
     * 删除文件或者目录
     * @param path 文件或目录路径
     */
    void delete(String path);

    /**
     * 复制本地文件到远端(hdfs、ftp ...)
     * @param localPath 本地文件名
     * @param remotePath 远端目录
     */
    void copyFromLocalFile(String localPath, String remotePath);

    Connection getConnection();

    String getSourceType();
}
