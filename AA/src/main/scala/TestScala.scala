import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.{Admin, ColumnFamilyDescriptor, ColumnFamilyDescriptorBuilder, Connection, ConnectionFactory, Get, Put, Result, ResultScanner, Scan, Table, TableDescriptor, TableDescriptorBuilder}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.security.UserGroupInformation

object TestScala {

  def main(args: Array[String]): Unit = {
    println("Hello world!")

    /*val spark = SparkSession.builder().appName("spark-hive").enableHiveSupport().getOrCreate();

    spark.sql("use test");
    val top100FlowUsersDF = spark.sql("select count(1) row_count, sum(pv) sum_pv, sum(flow) sum_flow, phone from app group by phone order by sum_flow desc limit 100");
    val exportJdbcUrl = "jdbc:mysql://192.168.3.23:3306/shangsong";
    val exportConnectionProperties = new Properties;
    exportConnectionProperties.put("user", "root");
    exportConnectionProperties.put("password", "Root$123");
    top100FlowUsersDF.write.jdbc(exportJdbcUrl, "top_100_flow_users", exportConnectionProperties);*/

    val connection = getConnection
    val admin = connection.getAdmin

    val table = connection.getTable(TableName.valueOf(TABLE_NAME))

    System.out.println("----------------------------------")

    //createTable(admin);//createTable(admin);

    //deleteTable(admin);//deleteTable(admin);

    insertData(connection);


    selectTable(table)

    System.out.println("---------------------------------------------------------------")

    val scan = new Scan
    val tableScan = connection.getTable(TableName.valueOf("user001"))
    scan.withStartRow(Bytes.toBytes("1001")) // 设置开始的行

    scan.withStopRow(Bytes.toBytes("1003" + "\u0001")) // 设置结束的行

    //scan.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("col1")); // 设置要查询的列族和列//scan.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("col1")); // 设置要查询的列族和列
    //scan.setCaching(100); // 设置缓存大小//scan.setCaching(100); // 设置缓存大小

    val scanner = tableScan.getScanner(scan)
    var result = scanner.next
    while (result != null) {
      // 处理查询结果
      for (cell <- result.rawCells) {
        System.out.print(Bytes.toString(cell.getQualifierArray, cell.getQualifierOffset, cell.getQualifierLength))
        System.out.println("=>" + Bytes.toString(cell.getValueArray, cell.getValueOffset, cell.getValueLength))
      }
      val rowkey = result.getRow
      System.out.println("Rowkey===========>: " + Bytes.toString(rowkey))
      result = scanner.next
    }

    scanner.close()
    tableScan.close()

    // 关闭资源// 关闭资源
    table.close()
    connection.close()
  }


  val TABLE_NAME = "test111"
  val COLUMN_FAMILY = "cf1"
  val COLUMN_NAME = "id"
  val ROW_KEY = "1001"
  val zookeeperQuorum = "bigbang-180,bigbang-181,bigbang-182"
  val clientPort = "2181"
  val znodeParent = "/hbase"
  val principal = "hdfs/bigbang-180@BIGBANG.COM"
  val kerberosConfPath = "D:\\Project\\SS-Fighting\\AA\\src\\main\\resources\\krb5.conf"
  val keytabPath = "D:\\Project\\SS-Fighting\\AA\\src\\main\\resources\\hdfs.keytab"


  @throws[Exception]
  def getConnection: Connection = {
    var connection: Connection = null
    val conf = HBaseConfiguration.create
    conf.set("hbase.zookeeper.quorum", "bigbang-180,bigbang-181,bigbang-182")
    conf.set("hbase.zookeeper.property.clientPort", "2181")
    conf.set("zookeeper.znode.parent", "/hbase")
    conf.setInt("hbase.rpc.timeout", 20000)
    conf.setInt("hbase.client.operation.timeout", 30000)
    conf.setInt("hbase.client.scanner.timeout.period", 200000)
    conf.set("hadoop.security.authentication", "kerberos")
    conf.set("hbase.security.authentication", "kerberos")
    System.setProperty("javax.security.auth.useSubjectCredsOnly", "false")
    // System.setProperty("java.security.krb5.conf", "file:///etc/krb5.conf")
    System.setProperty("java.security.krb5.conf", "D:\\Project\\SS-Fighting\\AA\\src\\main\\resources\\krb5.conf")
    // 使用票据登陆Kerberos
    UserGroupInformation.setConfiguration(conf)
    UserGroupInformation.loginUserFromKeytab("hdfs/bigbang-180@BIGBANG.COM", "D:\\Project\\SS-Fighting\\AA\\src\\main\\resources\\hdfs.keytab")
    connection = ConnectionFactory.createConnection(conf)
    connection
  }


  private def selectTable(table: Table): Unit = {
    // 创建Get对象，指定rowkey
    val get = new Get(Bytes.toBytes(ROW_KEY))
    // 添加查询的列族和列名
    //get.addColumn(Bytes.toBytes(COLUMN_FAMILY), Bytes.toBytes(COLUMN_NAME));
    // 查询数据
    val result = table.get(get)
    // 遍历查询结果
    for (cell <- result.rawCells) {
      System.out.print(Bytes.toString(cell.getQualifierArray, cell.getQualifierOffset, cell.getQualifierLength))
      System.out.println("=>" + Bytes.toString(cell.getValueArray, cell.getValueOffset, cell.getValueLength))
    }
  }

  private def insertData(connection: Connection): Unit = {
    val tableName = connection.getTable(TableName.valueOf("test111"))
    val put = new Put(Bytes.toBytes("1001"))
    put.addColumn(Bytes.toBytes("cf1"), Bytes.toBytes("address"), Bytes.toBytes("nj"))
    tableName.put(put)
  }

  private def deleteTable(admin: Admin): Unit = {
    val tableName = TableName.valueOf("test_create_table1")
    // 2.如果存在，禁用表
    if (admin.tableExists(tableName)) {
      //禁用表
      admin.disableTable(tableName)
      //3.删除表
      admin.deleteTable(tableName)
    }
    else System.out.println("表不存在!")
  }


  private def createTable(admin: Admin): Unit = {
    val tableName = TableName.valueOf("user001")
    // 2.表描述构建器
    val tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(tableName)
    // 3.列簇描述构建器
    // 创建列族描述符
    val cf1 = ColumnFamilyDescriptorBuilder.of("cf1")
    val cf2 = ColumnFamilyDescriptorBuilder.of("cf2")
    // 添加更多列族
    // 创建表描述符
    val tableDescriptor = TableDescriptorBuilder.newBuilder(tableName).setColumnFamily(cf1).setColumnFamily(cf2).build
    // 5.创建表
    admin.createTable(tableDescriptor)
    System.out.println("创建表" + tableName.getNameAsString + "成功")
  }
}
