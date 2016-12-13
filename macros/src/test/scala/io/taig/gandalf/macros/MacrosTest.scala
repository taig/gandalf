package io.taig.gandalf.macros

import io.taig.gandalf._
import io.taig.gandalf.macros.conversion.obeys._
import shapeless.test.illTyped

class MacrosTest extends Suite {
    "lift" should "lift values at compile time" in {
        lift( "foo" )( condition.success ).value shouldBe "foo"
    }

    it should "fail to lift values at compile time" in {
        illTyped( """lift( "foo" )( condition.failure )""" )
    }

    "@obeys" should "lift case class parameters at compile time" in {
        ClassSuccess( "foo" ).value.value shouldBe "foo"
    }

    it should "fail to lift case class parameters at compile time" in {
        illTyped( """ClassFailure( "foo" )""" )
    }
}