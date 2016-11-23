package io.taig.gandalf.core.test

import io.taig.gandalf.core.syntax.validation._

class RuleTest extends Suite {
    it should "provide a String representation" in {
        condition.success.serialize shouldBe "success"
        condition.failure.serialize shouldBe "failure"
    }

    "Condition" should "confirm input based on Rules" in {
        "foo".confirm( condition.success ) shouldBe Some( "foo" )
        "foo".confirm( condition.failure ) shouldBe None
    }

    "Mutation" should "mutate input based on Rules" in {
        "foo".confirm( mutation.success ) shouldBe Some( "foo" )
        "foo".confirm( mutation.failure ) shouldBe None
    }

    "Transformation" should "transform (mutate safely) input based on Rules" in {
        "foo".confirm( transformation ) shouldBe Some( "foo" )
    }
}