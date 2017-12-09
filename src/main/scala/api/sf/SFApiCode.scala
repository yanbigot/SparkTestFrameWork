package api.sf

import scala.io.Source

object SFApiCode {

  val fname = "D:\\DL\\s4s\\spark-scala-master\\spark-scala-master\\SparkTestFrameWork\\src\\main\\resources\\SFApi.properties"
  val USERNAME = "userName"
  val COMPANY_ID = "companyId"
  val PASSWORD = "password"
  val URL = "url"
  val BASIC_AUTH = "basicAuth"

  def getProperty(prop: String): String ={
    Source.fromFile(fname).getLines.filter(l => l.contains(prop)).map(_.split(":")(1)).mkString
  }
}
