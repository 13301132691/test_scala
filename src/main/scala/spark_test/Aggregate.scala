//package spark_test
//
//import breeze.linalg.max
//import org.apache.spark.{SparkConf, SparkContext}
//
///**
//  * Created by jax on 17-7-4.
//  */
//object Aggregate extends App{
//  val conf = new  SparkConf().setMaster("local").setAppName("AggregateTest")
//  val sc = new SparkContext(conf)
//  val arr = sc.parallelize(Array(1,2,3,4,5,6))
//  val result = arr.aggregate(0)(max(_,_),_+_)
//  print(result)
//}
