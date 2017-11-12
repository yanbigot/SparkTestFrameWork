package scalatest

import org.apache.spark.sql.Dataset

import scalatest.model.Pph

object Transform extends Serializable {

  val spark = App.sparkSession
  import spark.implicits._

  def incBy100(value: Int): Int = value + 1

  def addCompanyName(name: String): String = name + " - SG"

  def incrementId(ds: Dataset[Pph], incFunction: Int => Int = incBy100, addCompanyNameFunction: String => String = addCompanyName): Dataset[Pph] = {
    ds.map(pph => Pph(incFunction(pph.id), addCompanyNameFunction(pph.name), pph.age, pph.s))
  }


}
