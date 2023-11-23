object TestScala {

  def main(args: Array[String]): Unit = {
    println("Hello world!")
    val test1 = new Test1
    println(test1.da(3, "ddaed"))
    /*val spark = SparkSession.builder().appName("spark-hive").enableHiveSupport().getOrCreate();

    spark.sql("use test");
    val top100FlowUsersDF = spark.sql("select count(1) row_count, sum(pv) sum_pv, sum(flow) sum_flow, phone from app group by phone order by sum_flow desc limit 100");
    val exportJdbcUrl = "jdbc:mysql://192.168.3.23:3306/shangsong";
    val exportConnectionProperties = new Properties;
    exportConnectionProperties.put("user", "root");
    exportConnectionProperties.put("password", "Root$123");
    top100FlowUsersDF.write.jdbc(exportJdbcUrl, "top_100_flow_users", exportConnectionProperties);*/

  }
}
