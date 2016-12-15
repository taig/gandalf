package io.taig.gandalf.macros

import io.taig.gandalf._
import io.taig.gandalf.macros.conversion.obeys._
import shapeless.test.illTyped

class MacrosTest extends Suite {
    "lift" should "lift values at compile time into types" in {
        lift.fromType[condition.success]( "foo" ).value shouldBe "foo"
    }

    it should "fail to lift values at compile time into types" in {
        illTyped( """lift.fromType[condition.failure]( "foo" )""" )
    }

    it should "lift values at compile time into rules" in {
        lift.fromRule( condition.success )( "foo" ).value shouldBe "foo"
    }

    it should "fail to lift values at compile time into rules" in {
        illTyped( """lift.fromRule( condition.failure )( "foo" )""" )
    }

    it should "lift values at compile time into Obeys" in {
        lift.into[Name]( "foo" ).value shouldBe "foo"
    }

    it should "fail to lift values at compile time into Obeys" in {
        illTyped( """lift.into[Name]( 3 )""" )
    }

    "@obeys" should "lift case class parameters at compile time" in {
        ClassSuccess( "foo" ).value.value shouldBe "foo"
    }

    it should "fail to lift case class parameters at compile time" in {
        illTyped( """ClassFailure( "foo" )""" )
    }

    it should "lift to type aliases" in {
        val value: Name = "foobar"
        value.value shouldBe "foobar"
    }
}