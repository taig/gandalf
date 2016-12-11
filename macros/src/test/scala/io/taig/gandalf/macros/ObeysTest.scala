package io.taig.gandalf.macros

import io.taig.gandalf._
import shapeless.test.illTyped

class ObeysTest extends Suite {
    it should "forward toString to the underlying value" in {
        Obey.applyUnsafe[condition.success, String, String]( "foo" ).toString shouldBe
            "foo"
    }

    it should "convert implicitly unwrap" in {
        val value: String = Obey.applyUnsafe[condition.success, String, String]( "foo" )
        value shouldBe "foo"
    }

    it should "provide unapply" in {
        val Obey( value ) = Obey.applyUnsafe[condition.success, String, String]( "foo" )
        value shouldBe "foo"
    }

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