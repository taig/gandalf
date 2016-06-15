package io.taig.gandalf.tests

import cats.data.Validated._
import io.taig.gandalf.predef.IsDefined.isDefined
import io.taig.gandalf.predef.Matches.matches
import io.taig.gandalf.predef.Regex
import io.taig.gandalf.predef.Required.required
import io.taig.gandalf.predef.ToLowerCase.toLowerCase
import io.taig.gandalf.predef.Trim.trim
import io.taig.gandalf.predef.messages._
import io.taig.gandalf.syntax.all._

class PredefTest extends Suite {
    "IsDefined" should "validate Options" in {
        isDefined[String].validate( Some( "foo" ) ) shouldBe valid( "foo" )
        isDefined[String].validate( None ) shouldBe invalidNel( "IsDefined" )
    }

    "Matches" should "check for equality" in {
        matches( "foo" ).validate( "foo" ) shouldBe valid( "foo" )
        matches( "foo" ).validate( "foobar" ) shouldBe invalidNel( "Matches" )
    }

    "Regex" should "check if a given pattern matches" in {
        Regex.regex( ".*foo.*" ).validate( "foo" ) shouldBe valid( "foo" )
        Regex.regex( ".*foo.*" ).validate( "foobar" ) shouldBe valid( "foobar" )
        Regex.regex( ".*foo.*" ).validate( "bar" ) shouldBe invalidNel( "Regex" )
    }

    "Required" should "check whether a String is empty" in {
        required.validate( "foo" ) shouldBe valid( "foo" )
        required.validate( " " ) shouldBe valid( " " )
        required.validate( "" ) shouldBe invalidNel( "Required" )
    }

    "ToLowerCase" should "replace all uppercase letters in a String" in {
        toLowerCase.validate( "foo" ) shouldBe valid( "foo" )
        toLowerCase.validate( "Foo" ) shouldBe valid( "foo" )
        toLowerCase.validate( "FooBar" ) shouldBe valid( "foobar" )
    }

    "Trim" should "remove all trailing whitespace from a String" in {
        trim.validate( "foo" ) shouldBe valid( "foo" )
        trim.validate( "  foo" ) shouldBe valid( "foo" )
        trim.validate( "foo   " ) shouldBe valid( "foo" )
        trim.validate( "  foo   " ) shouldBe valid( "foo" )
        trim.validate( "     " ) shouldBe valid( "" )
    }
}