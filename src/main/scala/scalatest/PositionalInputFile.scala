package scalatest

trait PositionalInputFile {



  def parseLine(line: String, pattern: String): Unit ={
    val dummyLine = "AAZ_123_ii"
    val patt = "([A-Z]{3})_([0-9]{3})_([a-z]{2})".r

    val result = patt.findAllMatchIn(dummyLine).toList
    println(result)
  }
}
