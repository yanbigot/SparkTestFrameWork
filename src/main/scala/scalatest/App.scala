package scalatest

import org.apache.spark.sql.SparkSession


object App {

  def sparkSession = {
    val spark = SparkSession
      .builder()
      .appName("Simple Spark Application")
      .master("local[*]")
      .getOrCreate()

    spark
  }

  def main( args: Array[String] ): Unit = {
    print("Spark App !!! ")

    val spark: SparkSession = sparkSession
    val filePath = "D:\\DL\\s4s\\spark-scala-master\\spark-scala-master\\SparkTestFrameWork\\src\\main\\resources\\DataframeLoader.csv"
    val df = new DataframeLoader(filePath).loading()
    df.show(10)

    import spark.implicits._

    df.select($"age", $"id")

    spark.stop()
  }


}
