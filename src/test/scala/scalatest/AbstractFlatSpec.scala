package scalatest

import org.scalatest.{BeforeAndAfter, FlatSpec, GivenWhenThen}

import scala.io.Source

trait AbstractFlatSpec  extends FlatSpec with GivenWhenThen with Serializable with BeforeAndAfter{

  val spark = App.sparkSession
  import spark.implicits._

  def showInputDataFromFile( filePath: String, headerMessage: String, footerMessage: String): Unit = {
    markup(headerMessage)
    for (line <- Source.fromFile(filePath ).getLines()){
      markup("|"+line+"|")
    }
    markup(footerMessage)
  }
}
