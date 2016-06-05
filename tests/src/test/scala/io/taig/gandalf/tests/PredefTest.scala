package io.taig.gandalf.tests

import cats.data.Validated._
import io.taig.gandalf.predef._
import io.taig.gandalf.predef.messages._
import io.taig.gandalf.syntax.all._

class PredefTest extends Suite {
    "IsDefined" should "validate Options" in {
        IsDefined[String].validate( Some( "foo" ) ) shouldBe valid( "foo" )
        IsDefined[String].validate( None ) shouldBe invalidNel( "IsDefined" )
    }

    "Matches" should "check for equality" in {
        Matches( "foo" ).validate( "foo" ) shouldBe valid( "foo" )
        Matches( "foo" ).validate( "foobar" ) shouldBe invalidNel( "Matches" )
    }

    "Regex" should "check if a given pattern matches" in {
        Regex( ".*foo.*" ).validate( "foo" ) shouldBe valid( "foo" )
        Regex( ".*foo.*" ).validate( "foobar" ) shouldBe valid( "foobar" )
        Regex( ".*foo.*" ).validate( "bar" ) shouldBe invalidNel( "Regex" )
    }

    "Required" should "check whether a String is empty" in {
        Required.validate( "foo" ) shouldBe valid( "foo" )
        Required.validate( " " ) shouldBe valid( " " )
        Required.validate( "" ) shouldBe invalidNel( "Required" )
    }

    "ToLowerCase" should "replace all uppercase letters in a String" in {
        ToLowerCase.validate( "foo" ) shouldBe valid( "foo" )
        ToLowerCase.validate( "Foo" ) shouldBe valid( "foo" )
        ToLowerCase.validate( "FooBar" ) shouldBe valid( "foobar" )
    }

    "Trim" should "remove all trailing whitespace from a String" in {
        Trim.validate( "foo" ) shouldBe valid( "foo" )
        Trim.validate( "  foo" ) shouldBe valid( "foo" )
        Trim.validate( "foo   " ) shouldBe valid( "foo" )
        Trim.validate( "  foo   " ) shouldBe valid( "foo" )
        Trim.validate( "     " ) shouldBe valid( "" )
    }
}