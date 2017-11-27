package sandbox

import java.text.SimpleDateFormat
import java.util.Calendar

import org.scalacheck.Gen

import scala.reflect.runtime.{universe => ru}


object Beach{

  def numGen(): Gen[Int] = Gen.chooseNum(1, 1000000)
  def strGen(): Gen[String] = Gen.alphaStr

  def getSymbols[T: ru.TypeTag]: List[ru.Symbol] = ru.typeOf[T].members.toList

  /**
    * Return a random Generator depending on the Type
    * @param typ
    * @return
    */
  def genRandom[T: ru.TypeTag](typ: T): Gen[T] = {

    typ.getClass match {
      case _: Class[String] =>
         strGen.asInstanceOf[Gen[T]]
      case _: Class[Int] =>
        numGen.asInstanceOf[Gen[T]]
    }
  }

  def main( args: Array[String] ): Unit = {
//    val gen = numGen
    //    println (gen.getClass)
    //
    //    getSymbols[Pph]
    //      .filter(!_.isMethod)
    //      .map(x=> x.info)
    //      .foreach(x => println (x +" --- " + x.getClass))
    //
    //    getSymbols[Pph]
    //      .filter(!_.isMethod)
    //      .map(x=> genRandom(x.typeSignature))
    //      .foreach(x=> println (x.getClass))
    //
    //      val filePath = "D:\\DL\\s4s\\spark-scala-master\\spark-scala-master\\SparkTestFrameWork\\src\\main\\resources\\DataframeLoader.csv"
    //    val s = Source.fromFile(filePath).getLines().mkString("\r\n")
    //    print(s)

    print("parseLine")
    parseLine("","")

    print(getDate())
  }

  def parseLine(line: String, pattern: String): Unit ={
    val dummyLine = "AAZ_123_ii"
    val patt = "([A-Z]{3})_([0-9]{3})_([a-z]{2})".r

    val result = patt.findAllMatchIn(dummyLine).toList
    println(result)
    patt.findAllMatchIn(dummyLine).foreach(println)
  }

  private def getDate(): String ={
    //date('Y-m-d').'T'.date('H:i:s').'.000'
    val today = Calendar.getInstance().getTime()

    // create the date/time formatters
    val yearMonthDay = new SimpleDateFormat("YYYY-MM-dd")
    val hourMinuteSeconde = new SimpleDateFormat("hh-mm-ss")

    val currentHour = yearMonthDay.format(today)      // 12
    val currentMinute = hourMinuteSeconde.format(today)  // 29

    println(currentHour, "T",currentMinute, ".000")
    new String(currentHour + "T" + currentMinute + ".000")
  }
}
