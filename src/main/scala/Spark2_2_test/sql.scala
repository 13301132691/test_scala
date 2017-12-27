package Spark2_2_test

import org.apache.spark.sql.SparkSession
import org.apache.spark.rdd.RDD
case class Person(name:String,age:Int)
class sql {

}
//object sql extends App{
//  val sc = SparkSession.builder().appName("spark2.2_test").master("local[1]").getOrCreate()
//  import org.apache.spark.rdd.RDD
//  val peopleRDD:RDD[Person] = sc.sparkContext.parallelize(Seq(Person("jax",18)))
//  val people = peopleRDD.toDS
//  val teenagers = people.where(age >=10).where(age<=19).select(name).as[String]
//  teenagers.show
//  people.createOrReplaceTempView("people")
//  sc.read.format("json").load("input-json").select("name","score").where($"score">15).write.format("csv").save("output-csv")
//  import sc.implicits._
//  sc.read.json
//}