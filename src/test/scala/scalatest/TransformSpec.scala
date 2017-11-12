package scalatest

import org.apache.spark.sql.SparkSession

import scalatest.model.Pph

class TransformSpec extends AbstractSpec{

  markup  {
    """
       Here we test the transforming function as the program is based on the ETL pattern
       First we test the unitary transformation function
       Then we test the whole thing
    """
  }

  "Increment function " should " increment id by 100  " in {
    Given(" number 1")
    val string = "A"

    When(" calling function")
    val result = Transform.addCompanyName(string)

    Then(" company name has been added")
    assert(result == "A"+ " - SG")

    markup("That s all folks!")
  }

  "Add company function " should " add - SG to the name  " in {
    Given(" string A")
    val number = 1

    When(" calling function")
    val result = Transform.incBy100(number)

    Then(" result is  1 + 1")
    assert(result == 2)

    markup("That s all folks!")
  }

  "Transformer " should " add company name and increment id by 100  " in {
    Given(" one person")
    val pph = Pph(1, "Gloups", 32, "z")
    And("a dummy dataset")
    import spark.implicits._
    val ds = spark.createDataset(Seq(pph)).as[Pph]

    When(" applying transformation")
    val transformed = Transform.incrementId(ds, x => x + 100)

    Then(" company name has been added")
    assert(transformed.filter(pph => pph.name.contains("SG")).count() == 1)
    And(" id incremented by 100 ")
    assert(transformed.filter(pph => pph.id > 100).count() == 1)

    markup("That s all folks!")
  }
}
