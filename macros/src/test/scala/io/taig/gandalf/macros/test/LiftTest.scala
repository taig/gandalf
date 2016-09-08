package io.taig.gandalf.macros.test

import io.taig.gandalf.core.syntax.all._
import io.taig.gandalf.core.test._
import io.taig.gandalf.macros._
import shapeless.test.illTyped

class LiftTest extends Suite {
    it should "return the successfully validated result" in {
        lift( "foo" )( condition.success ).value shouldBe "foo"
        lift( "foo" )( transformation ~> condition.success ).value shouldBe "foo"
        lift( "foo" )( transformation ~> mutation.success ~> condition.success )
            .value shouldBe "foo"
    }

    it should "trigger a compiler error when validation fails" in {
        illTyped {
            """lift( "foo" )( condition.failure )"""
        }
    }

    it should "infer the input type" in {
        lift( "foo" )( generic.instance ~> condition.success )
            .value shouldBe "foo"
    }
}