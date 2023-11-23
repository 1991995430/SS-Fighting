package com.ss.song.test;

/**
 * author shangsong 2023/11/13
 */
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.security.UserGroupInformation;

public class Test1113 {

    //static Configuration conf = HBaseConfiguration.create();
    private static final String TABLE_NAME = "ss1";
    private static final String COLUMN_FAMILY = "cf1";
    private static final String COLUMN_NAME = "id";
    private static final String ROW_KEY = "1001";
    private static final String ZOOKEEPER_QUORUM = "192.168.3.180"; // 你的Zookeeper集群地址
    private static final int ZOOKEEPER_CLIENT_PORT = 2181; // 你的Zookeeper客户端端口
    /*
    private static final String TABLE_NAME = "ss1";
    private static final String COLUMN_FAMILY = "cf1";
    private static final String COLUMN_NAME = "id";
    private static final String ROW_KEY = "1001";
    private static final String ZOOKEEPER_QUORUM = "192.168.3.180"; // 你的Zookeeper集群地址
    private static final int ZOOKEEPER_CLIENT_PORT = 2181; // 你的Zookeeper客户端端口

    public static void main(String[] args) throws IOException {
        // 创建配置对象

        *//*configuration.set("hbase.zookeeper.quorum", ZOOKEEPER_QUORUM);
        configuration.set("hbase.zookeeper.property.clientPort", String.valueOf(ZOOKEEPER_CLIENT_PORT));*//*
        // 创建连接对象
        Connection connection = ConnectionFactory.createConnection(configuration);

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
    }*/
    /*private static Configuration conf = null;

    static {
        String rootPath = System.getProperty("user.dir");
        String keytab = rootPath + "/src/main/resources/hdfs.keytab";
        String krb5 = rootPath + "/src/main/resources/krb5.conf";
        System. setProperty("java.security.krb5.conf", krb5);
        conf = HBaseConfiguration.create();
        conf.set("hadoop.security.authentication" , "Kerberos");
        // 这个hbase.keytab也是从远程服务器上copy下来的, 里面存储的是密码相关信息
        // 这样我们就不需要交互式输入密码了
        conf.set("keytab.file" , keytab );
        // 这个可以理解成用户名信息，也就是Principal
        conf.set("kerberos.principal" , "hdfs/bigbang-183@BIGBANG.COM" );

        UserGroupInformation. setConfiguration(conf);
        try {
            UserGroupInformation. loginUserFromKeytab("hdfs/bigbang-183@BIGBANG.COM", keytab );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public static void main(String[] args) throws Exception {
        //Configuration conf = new Configuration();
        //conf.set("hbase.zookeeper.quorum", "bigbang-180");
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

