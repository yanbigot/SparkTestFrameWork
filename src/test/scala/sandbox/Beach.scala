package sandbox

import org.scalacheck.Gen

import scala.io.Source
import scala.reflect.runtime.{universe => ru}
import scalatest.model.Pph


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
    val gen = numGen
    println (gen.getClass)

    getSymbols[Pph]
      .filter(!_.isMethod)
      .map(x=> x.info)
      .foreach(x => println (x +" --- " + x.getClass))

    getSymbols[Pph]
      .filter(!_.isMethod)
      .map(x=> genRandom(x.typeSignature))
      .foreach(x=> println (x.getClass))

      val filePath = "D:\\DL\\s4s\\spark-scala-master\\spark-scala-master\\SparkTestFrameWork\\src\\main\\resources\\DataframeLoader.csv"
    val s = Source.fromFile(filePath).getLines().mkString("\r\n")
    print(s)
  }
}
