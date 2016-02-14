package io.taig.bsts.tests

import io.taig.bsts.transformation.{ string, Transformation }

class TransformationTest extends Suite {
    "apply" should "allow to create a transformation from a Function" in {
        val transformation = Transformation[String, String]( "trim" )( _.trim )

        transformation.validate( "asdf" ) shouldBe "asdf"
        transformation.validate( "  asdf " ) shouldBe "asdf"
        transformation.validate( "" ) shouldBe ""
    }

    "trim" should "... well, it should trim a String" in {
        string.trim.validate( "asdf" ) shouldBe "asdf"
        string.trim.validate( "  asdf" ) shouldBe "asdf"
        string.trim.validate( "asdf  " ) shouldBe "asdf"
        string.trim.validate( "  asdf  " ) shouldBe "asdf"
        string.trim.validate( "" ) shouldBe ""
    }
}