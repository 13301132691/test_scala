//package spark_test
//
///**
//  * Created by jax on 17-6-27.
//  */
//import org.apache.spark.SparkContext._
//import org.apache.spark.{SparkConf, SparkContext}
//
//object Sogou_test extends App{
//    if (args.length == 0) {
//      System.err.println ( "Usage: SogouResult <file1> <file2>" )
//      System.exit ( 1 )
//    }
//    val conf = new SparkConf ().setAppName ( "Sogou_text" ).setMaster ( "local" )
//    val sc = new SparkContext ( conf )
//    //session 查询次数排行榜
//    val rdd1 = sc.textFile ( args ( 0 ) ).map ( _.split ( "\t" ) ).filter ( _.length == 6 )
//    val rdd2 = rdd1.map ( x => (x ( 1 ), 1) ).reduceByKey ( _ + _ ).map ( x => (x._2, x._1) ).sortByKey ( false )
//      .map ( x => (x._2, x._1) )
//    rdd2.saveAsTextFile ( args ( 1 ) )
//    sc.stop ()
//}
