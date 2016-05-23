package io.taig.gandalf.tests

import cats.data.NonEmptyList
import cats.data.Validated._
import io.taig.gandalf.implicits._
import io.taig.gandalf.predef.Matches.matches
import io.taig.gandalf.predef.Required.required
import io.taig.gandalf.predef.Trim
import io.taig.gandalf.predef.Trim.trim

class SyntaxTest extends Suite {
    "~>" should "allow to mutate data" in {
        ( trim ~> required ).validate( "foo" ) shouldBe valid( "foo" )
        ( trim ~> required ).validate( " foo  " ) shouldBe valid( "foo" )
        ( trim ~> required ).validate( "" ) shouldBe invalidNel( "required" )
        ( trim ~> required ).validate( "   " ) shouldBe invalidNel( "required" )
    }

    "&&" should "mimic the logical AND" in {
        ( required && matches( "foo" ) ).validate( "foo" ) shouldBe valid( "foo" )
        ( required && matches( "foo" ) ).validate( "foobar" ) shouldBe invalidNel( "matches" )
        ( required && matches( "foo" ) ).validate( "" ) shouldBe invalidNel( "required" )
    }

    "&" should "mimic the bitwise AND" in {
        ( required & matches( "foo" ) ).validate( "foo" ) shouldBe valid( "foo" )
        ( required & matches( "foo" ) ).validate( "foobar" ) shouldBe invalidNel( "matches" )
        ( required & matches( "foo" ) ).validate( "" ) shouldBe invalid( NonEmptyList( "required", "matches" ) )
    }

    "||" should "mimic the logical OR" in {
        ( required || matches( "foo" ) ).validate( "foo" ) shouldBe valid( "foo" )
        ( required || matches( "foo" ) ).validate( "foobar" ) shouldBe valid( "foobar" )
        ( required || matches( "foo" ) ).validate( "" ) shouldBe invalid( NonEmptyList( "required", "matches" ) )
    }
}