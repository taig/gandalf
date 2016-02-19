package io.taig.bsts.tests

import cats.data.Validated.Valid
import io.taig.bsts.predef._

class MutationTest extends Suite {
    "isDefined" should "verify that an Option is not empty" in {
        option.isDefined.validate( Some( "foobar" ) ).isValid shouldBe true
        option.isDefined.validate( None ).isValid shouldBe false
    }

    "parse" should "extract Ints" in {
        string.parse[Int].validate( "5" ) shouldBe Valid( 5 )
        string.parse[Int].validate( "foo" ).isValid shouldBe false
    }

    it should "extract Floats" in {
        string.parse[Float].validate( "5.2" ) shouldBe Valid( 5.2f )
        string.parse[Float].validate( "foo" ).isValid shouldBe false
    }

    it should "extract Longs" in {
        string.parse[Long].validate( "5" ) shouldBe Valid( 5l )
        string.parse[Long].validate( "foo" ).isValid shouldBe false
    }

    it should "extract Doubles" in {
        string.parse[Double].validate( "5.0" ) shouldBe Valid( 5.0d )
        string.parse[Double].validate( "foo" ).isValid shouldBe false
    }
}