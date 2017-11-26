package api

import scala.language.dynamics
import scala.io.Source

object ApiCode {
  val fname = "D:\\DL\\s4s\\spark-scala-master\\spark-scala-master\\SparkTestFrameWork\\src\\main\\resources\\api.properties"
  val API_SECRET = "apiSecret"
  val API_ID = "apiId"
  val USER_NAME = "userName"

  def getProperty(prop: String): String ={
    Source.fromFile(fname).getLines.filter(l => l.contains(prop)).map(_.split("=")(1)).mkString
  }

  def main( args: Array[String] ): Unit = {
    println(getProperty(API_SECRET))
    println(getProperty(API_ID))
    println(getProperty(USER_NAME))
  }
}
