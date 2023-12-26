package com.ss.song.test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.security.UserGroupInformation;

/**
 * author shangsong 2023/11/20
 */
public class Test1120 {
    private static final String TABLE_NAME = "ss1";
    private static final String COLUMN_FAMILY = "cf1";
    private static final String COLUMN_NAME = "id";
    private static final String ROW_KEY = "1001";

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("hbase.zookeeper.quorum", "bigbang-180,bigbang-181,bigbang-182");
        UserGroupInformation.setConfiguration(conf);
        UserGroupInformation.loginUserFromKeytab("hdfs/bigbang-180@EXAMPLE.COM", "/etc/pbh/keytabs/hdfs.keytab");

        UserGroupInformation ugi = UserGroupInformation.getCurrentUser();
        System.out.println("Authenticated user: " + ugi.getUserName());

        Connection connection = ConnectionFactory.createConnection(conf);

        Admin admin = connection.getAdmin();
        System.out.println(admin);

        // 获取表对象
        Table table = connection.getTable(TableName.valueOf(TABLE_NAME));
        // 创建Get对象，指定rowkey
        Get get = new Get(Bytes.toBytes(ROW_KEY));
        // 添加查询的列族和列名
        get.addColumn(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes(COLUMN_NAME));
        // 查询数据
        Result result = table.get(get);
        // 遍历查询结果
        for (Cell cell : result.rawCells()) {
            System.out.println("Cell: " + cell);
            System.out.println("Value: " + Bytes.toString(CellUtil.cloneValue(cell)));
        }
        // 关闭资源
        table.close();
        connection.close();
    }

}
