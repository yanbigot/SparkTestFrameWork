package scalatest

import org.scalatest.{FlatSpec, GivenWhenThen}

trait AbstractSpec  extends FlatSpec with GivenWhenThen with Serializable{

  val spark = App.sparkSession
  import spark.implicits._
}
