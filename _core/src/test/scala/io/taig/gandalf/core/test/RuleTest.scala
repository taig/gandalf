package io.taig.gandalf.core.test

import cats.data.Validated._
import io.taig.gandalf.core.implicits._

class RuleTest extends Suite {
    it should "provide a toString representation" in {
        condition.success.serialize shouldBe "success"
        condition.failure.serialize shouldBe "failure"
        mutation.success.serialize shouldBe "success"
        mutation.failure.serialize shouldBe "failure"
    }

    "Condition" should "verify properties" in {
        condition.success.validate( "foo" ) shouldBe valid( "foo" )
        condition.failure.validate( "foo" ) shouldBe invalidNel( "condition" )
    }

    "Mutation" should "mutate data" in {
        mutation.success.validate( "foo" ) shouldBe valid( "foo" )
        mutation.failure.validate( "foo" ) shouldBe invalidNel( "container" )
    }

    "Transformation" should "mutate data safely" in {
        transformation.validate( "foo" ) shouldBe valid( "foo" )
    }
}