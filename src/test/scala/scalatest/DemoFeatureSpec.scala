package scalatest

import org.scalatest.{FeatureSpec, GivenWhenThen}

class DemoFeatureSpec extends FeatureSpec with GivenWhenThen{

  markup("Fully respect the BDD Gerkhin style")

  feature("The user can pop an element off the top of the stack") {

    info("As a programmer")
    info("I want to be able to pop items off the stack")
    info("So that I can get them in last-in-first-out order")

    scenario("pop is invoked on a non-empty stack") {

      Given("a non-empty stack")
      When("when pop is invoked on the stack")
      Then("the most recently pushed element should be returned")
      And("the stack should have one less item than before")
      pending
    }

    scenario("pop is invoked on an empty stack") {

      Given("an empty stack")
      When("when pop is invoked on the stack")
      Then("NoSuchElementException should be thrown")
      And("the stack should still be empty")
      pending
    }
  }
}
