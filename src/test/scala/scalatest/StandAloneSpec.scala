package scalatest

import org.scalatest._

import scalatest.model.Person

class StandAloneSpec extends FlatSpec with GivenWhenThen{

  markup  {
    """
      This is living documentation !!!!
    """
  }

  "Hanane " should " belongs to this " in {
    Given("sequence of Person")
    val person1 = Person("raoul", 32)
    val person2 = Person("hanane", 40)

    When(" doing nothing ")

    Then(" age is not modified ")
    assert(person2.age === 40)

    markup("This test finished with a **bold** statement!")
  }
}
