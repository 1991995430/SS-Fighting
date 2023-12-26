package com.ss.song.controller;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.security.UserGroupInformation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;


@RestController
@RequestMapping("/v1/hbase")
public class HbaseController {
    private static final String TABLE_NAME = "ssspace:ss1";
    private static final String COLUMN_FAMILY = "cf1";
    private static final String COLUMN_NAME = "id";
    private static final String ROW_KEY = "1001";
    private String zookeeperQuorum = "bigbang-180,bigbang-181,bigbang-182";
    private String clientPort = "2181";
    private String znodeParent = "/hbase";
    private String kerberosConfPath = "D:\\Project\\SS-Fighting\\ss-service\\ss-trino\\src\\main\\resources\\krb5.conf";
    private String principal = "hdfs/bigbang-180@BIGBANG.COM";
    private String keytabPath = "D:\\Project\\SS-Fighting\\ss-service\\ss-trino\\src\\main\\resources\\hdfs.keytab";

    @PostMapping("/con1")
    public String con1() {
        try (Connection connection = getConnection();Table table = connection.getTable(TableName.valueOf(TABLE_NAME))) {

            Admin admin = connection.getAdmin();

            System.out.println("----------------------------------");

            //createTable(admin);

            //deleteTable(admin);

            //insertData(connection);


            return selectTable(table);


            // 关闭资源
            //table.close();
            //return scanTable(connection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Configuration getHbaseConf() {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", zookeeperQuorum);
        conf.set("hbase.zookeeper.property.clientPort", clientPort);
        conf.set("zookeeper.znode.parent", znodeParent);
        conf.setInt("hbase.rpc.timeout", 20000);
        conf.setInt("hbase.client.operation.timeout", 30000);
        conf.setInt("hbase.client.scanner.timeout.period", 200000);
        conf.set("hadoop.security.authentication", "kerberos");
        conf.set("hbase.security.authentication", "kerberos");
        System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");
        System.setProperty("java.security.krb5.conf", kerberosConfPath);
        return conf;
    }

    public synchronized Connection getConnection() throws Exception {
        Connection connection = null;
        Configuration conf = getHbaseConf();
        // 使用票据登陆Kerberos
        UserGroupInformation.setConfiguration(conf);
        UserGroupInformation.loginUserFromKeytab(principal, keytabPath);

        connection = ConnectionFactory.createConnection(conf);
        return connection;
    }

    /*public static void main(String[] args) throws Exception {
        Connection connection = getConnection();
        Admin admin = connection.getAdmin();

        Table table = connection.getTable(TableName.valueOf(TABLE_NAME));

        System.out.println("----------------------------------");

        //createTable(admin);

        //deleteTable(admin);

        //insertData(connection);


        // selectTable(table);

        scanTable(connection);


        // 关闭资源
        table.close();
        connection.close();

    }*/

    private static String scanTable(Connection connection) throws IOException {
        StringBuilder sb = new StringBuilder();
        Scan scan = new Scan();
        Table tableScan = connection.getTable(TableName.valueOf("user001"));
        scan.withStartRow(Bytes.toBytes("1001")); // 设置开始的行
        scan.withStopRow(Bytes.toBytes("1004")); // 设置结束的行
        //scan.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("col1")); // 设置要查询的列族和列
        //scan.setCaching(100); // 设置缓存大小

        ResultScanner scanner = tableScan.getScanner(scan);
        for (Result result = scanner.next(); result != null; result = scanner.next()) {
            byte[] rowkey = result.getRow();
            sb.append("Rowkey===========>: " + Bytes.toString(rowkey));
            // 处理查询结果
            for (Cell cell : result.rawCells()) {
                //System.out.print(Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()));
                //System.out.println("=>" + Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
                sb.append(Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()))
                        .append("=>" + Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()))
                        .append("\n");
            }
            //System.out.println("Rowkey===========>: " + Bytes.toString(rowkey));

        }

        scanner.close();
        tableScan.close();
        return sb.toString();
    }

    private static String selectTable(Table table) throws IOException {
        // 创建Get对象，指定rowkey
        StringBuilder sb = new StringBuilder();
        Get get = new Get(Bytes.toBytes(ROW_KEY));
        // 添加查询的列族和列名
        //get.addColumn(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes(COLUMN_NAME));
        // 查询数据
        Result result = table.get(get);
        byte[] rowkey = result.getRow();
        sb.append("Rowkey===========>: " + Bytes.toString(rowkey));
        // 遍历查询结果
        for (Cell cell : result.rawCells()) {
            //System.out.print(Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()));
            //System.out.println("=>" + Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
            sb.append(Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()))
                    .append("=>" + Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()))
                    .append("\n");
        }
        return sb.toString();
    }

    private static void insertData(Connection connection) throws IOException {
        Table tableName = connection.getTable(TableName.valueOf("test_create_table"));
        Put put = new Put(Bytes.toBytes("1001"));
        put.addColumn(Bytes.toBytes("cf001"), Bytes.toBytes("name"), Bytes.toBytes("ss012"));
        tableName.put(put);
    }

    private static void deleteTable(Admin admin) throws IOException {
        TableName tableName = TableName.valueOf("test_create_table1");
        // 2.如果存在，禁用表
        if (admin.tableExists(tableName)) {
            //禁用表
            admin.disableTable(tableName);
            //3.删除表
            admin.deleteTable(tableName);
        } else {
            System.out.println("表不存在!");
        }
    }

    private static void createTable(Admin admin) throws IOException {
        TableName tableName = TableName.valueOf("user001");
        // 2.表描述构建器
        TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(tableName);
        // 3.列簇描述构建器
        // 创建列族描述符
        ColumnFamilyDescriptor cf1 = ColumnFamilyDescriptorBuilder.of("cf1");
        ColumnFamilyDescriptor cf2 = ColumnFamilyDescriptorBuilder.of("cf2");
        // 添加更多列族

        // 创建表描述符
        TableDescriptor tableDescriptor = TableDescriptorBuilder
                .newBuilder(tableName)
                .setColumnFamily(cf1)
                .setColumnFamily(cf2)
                .build();
        // 5.创建表
        admin.createTable(tableDescriptor);
        System.out.println("创建表" + tableName.getNameAsString() + "成功");
    }
}
