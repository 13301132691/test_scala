package spark_ml_cookbook

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{Dataset, SparkSession}
//import spark.ml.cookbook.chapter3.{Car, MyDatasetData}

//import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object c3_dataset{
  case class Car(make: String, model: String, price: Double, style: String, kind: String)
  val carData =
    Seq(
      Car("Tesla", "Model S", 71000.0, "sedan","electric"),
      Car("Audi", "A3 E-Tron", 37900.0, "luxury","hybrid"),
      Car("BMW", "330e", 43700.0, "sedan","hybrid"),
      Car("BMW", "i3", 43300.0, "sedan","electric"),
      Car("BMW", "i8", 137000.0, "coupe","hybrid"),
      Car("BMW", "X5 xdrive40e", 64000.0, "suv","hybrid"),
      Car("Chevy", "Spark EV", 26000.0, "coupe","electric"),
      Car("Chevy", "Volt", 34000.0, "sedan","electric"),
      Car("Fiat", "500e", 32600.0, "coupe","electric"),
      Car("Ford", "C-Max Energi", 32600.0, "wagon/van","hybrid"),
      Car("Ford", "Focus Electric", 29200.0, "sedan","electric"),
      Car("Ford", "Fusion Energi", 33900.0, "sedan","electric"),
      Car("Hyundai", "Sonata", 35400.0, "sedan","hybrid"),
      Car("Kia", "Soul EV", 34500.0, "sedan","electric"),
      Car("Mercedes", "B-Class", 42400.0, "sedan","electric"),
      Car("Mercedes", "C350", 46400.0, "sedan","hybrid"),
      Car("Mercedes", "GLE500e", 67000.0, "suv","hybrid"),
      Car("Mitsubishi", "i-MiEV", 23800.0, "sedan","electric"),
      Car("Nissan", "LEAF", 29000.0, "sedan","electric"),
      Car("Porsche", "Cayenne", 78000.0, "suv","hybrid"),
      Car("Porsche", "Panamera S", 93000.0, "sedan","hybrid"),
      Car("Tesla", "Model X", 80000.0, "suv","electric"),
      Car("Tesla", "Model 3", 35000.0, "sedan","electric"),
      Car("Volvo", "XC90 T8", 69000.0, "suv","hybrid"),
      Car("Cadillac", "ELR", 76000.0, "coupe","hybrid")
    )
  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR)
    Logger.getLogger("akka").setLevel(Level.ERROR)

    // setup SparkSession to use for interactions with Spark
    val spark = SparkSession
      .builder
      .master("local[*]")
      .appName("mydatasetfunc")
      .config("spark.sql.warehouse.dir", ".")
      .getOrCreate()

    import spark.implicits._

    val cars = spark.createDataset(carData)
    cars.show(false)

    val modelData = cars.groupByKey(_.make).mapGroups({
      case (make, car) => {
        val carModel = new ListBuffer[String]()
        car.map(_.model).foreach({
          c =>  carModel += c
        })
        (make, carModel)
      }
    })
    cars.write.json("cars.json")
    spark.stop()
  }
}
