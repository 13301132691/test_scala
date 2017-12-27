//package object Spark2_2_test {
//  import org.apache.spark.sql.SparkSession
//  private var _spark:SparkSession = SparkSession.builder().appName("spark2.2_test").master("local[1]").getOrCreate()
//  implicit def sc:SparkSession = {
//    if (_spark == null) throw ExceptionInInitializerError()
//    else _spark
//  }
//}
