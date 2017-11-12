package scalatest

import org.apache.spark.sql.DataFrame

class DataframeLoader( filePath:String) {

  def loading():DataFrame = {
    val spark = App.sparkSession

    spark
      .read
      .format("com.databricks.spark.csv")
      .option("header", "true")
      .option("inferSchema", "true")
      .load(filePath)
  }
}
