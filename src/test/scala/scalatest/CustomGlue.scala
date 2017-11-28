package scalatest

import scala.io.Source

object CustomGlue {

  def buildStub(filePath: String): Unit ={
    val fileLines = Source.fromFile(filePath).getLines
    val feature = fileLines.filter(l => l.contains("feature:")).map(_.split(":")(0)).mkString
    val scenario = fileLines.filter(l => l.contains("scenario:")).map(_.split(":")(0)).mkString

    println(feature)
    println(scenario)

    val featurePattern = """(?m)feature:()(.*)scenario:"""
    val map = Map((""->""))
    for (line <- Source.fromFile(filePath).getLines) {
      println(line)
      featurePattern.r.findAllIn( line ) match {
        case feat  => map + ("feature" -> feat)
        case _ => println("Not found !! ")
      }
    }

    println(map)

  }

  def findScenario(line: String): String ={
    var result = ""
    val results = for (m <- "feature:(.*)scenario:".r.findAllMatchIn(line)) yield m.group(1)
    results.foreach(x => result = x)
    result
  }

  def testFindScenario(): Unit ={
    val testString = "feature: GOOD RESULT scenario:"
    val result = findScenario(testString)
    println("result: "+ result)
  }

  def main( args: Array[String] ): Unit = {
    //buildStub("D:\\DL\\s4s\\spark-scala-master\\spark-scala-master\\SparkTestFrameWork\\src\\test\\resources\\toglue.feat")
    testFindScenario
  }
}
