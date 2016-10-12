package io.taig.gandalf.predef.test

import cats.data.NonEmptyList
import cats.data.Validated._
import io.taig.gandalf.core.syntax.all._
import io.taig.gandalf.core.test.Suite
import io.taig.gandalf.predef.numeric._

class NumericTest extends Suite {
    "gt" should "compare numbers with the > operator" in {
        gt( 5 ).validate( 10 ) shouldBe valid( 10 )
        gt( 5 ).validate( 5 ) shouldBe invalidNel( "gt" )
        gt( 5 ).validate( 0 ) shouldBe invalidNel( "gt" )
    }

    "gte" should "compare numbers with the >= operator" in {
        gte( 5 ).validate( 10 ) shouldBe valid( 10 )
        gte( 5 ).validate( 5 ) shouldBe valid( 5 )
        gte( 5 ).validate( 0 ) shouldBe
            invalid( NonEmptyList.of( "gt", "equal" ) )
    }

    "isZero" should "check if a number is == 0" in {
        isZero[Int].validate( 0 ) shouldBe valid( 0 )
        isZero[Int].validate( 5 ) shouldBe invalidNel( "isZero" )
        isZero[Int].validate( -5 ) shouldBe invalidNel( "isZero" )
    }

    "lt" should "compare numbers with the > operator" in {
        lt( 5 ).validate( 0 ) shouldBe valid( 0 )
        lt( 5 ).validate( 5 ) shouldBe invalidNel( "lt" )
        lt( 5 ).validate( 10 ) shouldBe invalidNel( "lt" )
    }

    "lte" should "compare numbers with the >= operator" in {
        lte( 5 ).validate( 0 ) shouldBe valid( 0 )
        lte( 5 ).validate( 5 ) shouldBe valid( 5 )
        lte( 5 ).validate( 10 ) shouldBe
            invalid( NonEmptyList.of( "lt", "equal" ) )
    }

    "negative" should "check if a number is < 0" in {
        negative[Int].validate( -5 ) shouldBe valid( -5 )
        negative[Int].validate( 0 ) shouldBe invalidNel( "negative" )
        negative[Int].validate( 5 ) shouldBe invalidNel( "negative" )
    }

    "positive" should "check if a number is > 0" in {
        positive[Int].validate( 5 ) shouldBe valid( 5 )
        positive[Int].validate( 0 ) shouldBe invalidNel( "positive" )
        positive[Int].validate( -5 ) shouldBe invalidNel( "positive" )
    }
}