package io.taig.gandalf.macros

import io.taig.gandalf.core.Suite
import shapeless.test.illTyped

class ObeysTest extends Suite {
    it should "lift case class parameters at compile time" in {
        ClassSuccess( "foo" ).value.value shouldBe "foo"
    }

    it should "fail to lift case class parameters at compile time" in {
        illTyped( """ClassFailure( "foo" )""" )
    }
}