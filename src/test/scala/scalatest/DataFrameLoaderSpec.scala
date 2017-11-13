package scalatest

import org.apache.spark.sql.{Dataset, SparkSession}

import scalatest.model.Pph

class DataFrameLoaderSpec extends AbstractFlatSpec{
  var filePath = "D:\\DL\\s4s\\spark-scala-master\\spark-scala-master\\SparkTestFrameWork\\src\\main\\resources\\DataframeLoader.csv"
  val headerMessage =  {
    """
      This is living documentation !!!!
      Here we do check if the loader is properly returning a dataset as a dataframe
      As the load is not by itself an action we ll need to make some transformation on it !
      We ll use a very simple test file in order to make it work, we re looking to retreive those informations
    """
  }
  val footerMessage =  {
    """==========================================================================================================
    """
  }

  /**
    * No need to write in fitnesse the input data
    */
  showInputDataFromFile(filePath, headerMessage, footerMessage)


  "A nice loader " should " return the list of  " in {
    Given("sequence of Person")

    And(" a nice spark session ")
    val spark = App.sparkSession
    import spark.implicits._

    When(" loading this file as a dataframe ")
      val df = new DataframeLoader(filePath)
        .loading()
    And(" getting the teenagers")
      val teenagers = df.where('age >= 10).where('age <= 19).select('name).as[String]
    And(" looking for Sofia in the dataframe")
      val sofia = {
        df.filter($"name" === "Sofia").select('name).as[String].collect()
      }
    And(" transforming the dataframe to a dataset ")
      val ds: Dataset[Pph] = df.as[Pph]
    And(" looking for person with id < 100 and more than 14 years old ")
      val goodIdAndNotTeens = ds.filter(x => x.id < 100 && x.age > 14)
    And(" looking for Sofia again but in the dataset ")
      val sofiaDs = ds.filter(x => x.name == "Sofia" )

    Then(" there is two person ")
      assert(df.count() == 2)
    And(" none is a teenager ")
      assert(teenagers.count() == 0)
    And(" one is named Sofia")
      assert(sofia.head == "Sofia")
    And(" it s the same with dataset: no teens ")
      assert(goodIdAndNotTeens.count() == 2)
    And(" it s the same with dataset: Sofia s here ")
      assert(sofiaDs.count() == 1)

    markup("That s all folks!")
  }
}