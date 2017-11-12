package scalatest.spark.testing.base
import com.holdenkarau.spark.testing.{DatasetGenerator, SharedSparkContext}
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll
import org.scalatest.FunSuite
import org.scalatest.prop.Checkers

import scala.reflect.runtime.{universe => yan}
import scalatest.App
import scalatest.model.Pph

class FunSuiteTestFirst extends FunSuite with SharedSparkContext with Checkers {

  markup {
    """ Technical objective : testing property based test and spark testing base
      |     val property =
      |      forAll(DatasetGenerator.genDataset[Pph](sql)(genPph)) {
      |        dataset =>
      |          dataset.show(100)
      |          dataset.map(_.id).count() == dataset.count()
      |      }
      |
      |    check(property)
    """.stripMargin
  }

  test("test generating Datasets[Physical person] based on case class") {
    val genPph: Gen[Pph] = pphGen

    val sql = App.sparkSession.sqlContext
    import sql.implicits._

   // val myGen = Gen(Gen.alphaStr, Gen.alphaStr)

    val property =
      forAll(DatasetGenerator.genDataset[Pph](sql)(genPph)) {
        dataset =>
          dataset.show(100)
          dataset.map(_.id).count() == dataset.count()
      }

    check(property)
  }

  def pphGen(): Gen[Pph] = {
    for {
      idGen <- Gen.chooseNum(1, 1000000)
      nameGen <- Gen.alphaStr
      ageGen <- Gen.chooseNum(1, 1000000)
      sGen <- Gen.alphaStr
    } yield Pph(
      id = idGen,
      name = nameGen,
      age = ageGen,
      s = sGen
    )
  }

}