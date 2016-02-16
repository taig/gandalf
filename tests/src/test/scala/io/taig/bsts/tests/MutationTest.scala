package io.taig.bsts.tests

import cats.data.Xor._
import io.taig.bsts.mutation._

class MutationTest extends Suite {
    "isDefined" should "verify that an Option is not empty" in {
        option.isDefined.validate( Some( "foobar" ) ).isRight shouldBe true
        option.isDefined.validate( None ).isRight shouldBe false
    }

    "parse" should "extract Ints" in {
        string.parse[Int].validate( "5" ) shouldBe Right( 5 )
        string.parse[Int].validate( "foo" ).isRight shouldBe false
    }

    it should "extract Floats" in {
        string.parse[Float].validate( "5.2" ) shouldBe Right( 5.2f )
        string.parse[Float].validate( "foo" ).isRight shouldBe false
    }

    it should "extract Longs" in {
        string.parse[Long].validate( "5" ) shouldBe Right( 5l )
        string.parse[Long].validate( "foo" ).isRight shouldBe false
    }

    it should "extract Doubles" in {
        string.parse[Double].validate( "5.0" ) shouldBe Right( 5.0d )
        string.parse[Double].validate( "foo" ).isRight shouldBe false
    }
}