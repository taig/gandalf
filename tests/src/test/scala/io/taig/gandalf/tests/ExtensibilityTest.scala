package io.taig.gandalf.tests

import cats.data.Validated._
import io.taig.gandalf._
import io.taig.gandalf.predef._
import io.taig.gandalf.predef.messages._
import io.taig.gandalf.syntax.all._
import shapeless.Witness

class ExtensibilityTest extends Suite {
    it should "be possible to create custom Actions with inheritance" in {
        trait MyRequired extends ( Trim <~> Required )
        object MyRequired extends MyRequired

        MyRequired.validate( "foo" ) shouldBe valid( "foo" )
        MyRequired.validate( "  foo   " ) shouldBe valid( "foo" )
        MyRequired.validate( "" ) shouldBe invalidNel( "Required" )
        MyRequired.validate( "    " ) shouldBe invalidNel( "Required" )
    }

    it should "be possible to create custom Actions with type aliases" in {
        type MyRequired = Trim <~> Required
        val MyRequired = new MyRequired

        MyRequired.validate( "foo" ) shouldBe valid( "foo" )
        MyRequired.validate( "  foo   " ) shouldBe valid( "foo" )
        MyRequired.validate( "" ) shouldBe invalidNel( "Required" )
        MyRequired.validate( "    " ) shouldBe invalidNel( "Required" )
    }

    it should "be possible to override Operation error messages" in {
        trait MyRequired extends ( IsDefined[String] <*> Required )

        object MyRequired extends MyRequired {
            implicit val error = Error.instance[MyRequired]( "foobar" )
        }

        MyRequired.validate( Some( "foobar" ) ) shouldBe valid( "foobar" )
        MyRequired.validate( Some( "" ) ) shouldBe invalidNel( "foobar" )
        MyRequired.validate( None ) shouldBe invalidNel( "foobar" )

        trait MyMatch extends ( Trim <~> Matches[Witness.`"foo"`.T, String] )

        object MyMatch extends MyMatch {
            implicit val error = Error.instance[MyMatch]( "foobar" )
        }

        MyMatch.validate( "foo" ) shouldBe valid( "foo" )
        MyMatch.validate( " foo  " ) shouldBe valid( "foo" )
        MyMatch.validate( "" ) shouldBe invalidNel( "foobar" )
    }
}