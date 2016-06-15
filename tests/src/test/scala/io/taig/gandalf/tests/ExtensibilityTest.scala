package io.taig.gandalf.tests

import cats.data.Validated._
import io.taig.gandalf._
import io.taig.gandalf.predef._
import io.taig.gandalf.predef.messages._
import io.taig.gandalf.syntax.all._
import shapeless.Witness

class ExtensibilityTest extends Suite {
    it should "be possible to create custom Actions with inheritance" in {
        class MyRequired extends ( Trim <~> Required )
        val myRequired = new MyRequired

        myRequired.validate( "foo" ) shouldBe valid( "foo" )
        myRequired.validate( "  foo   " ) shouldBe valid( "foo" )
        myRequired.validate( "" ) shouldBe invalidNel( "Required" )
        myRequired.validate( "    " ) shouldBe invalidNel( "Required" )
    }

    it should "be possible to create custom Actions with type aliases" in {
        type MyRequired = Trim <~> Required
        val myRequired = new MyRequired

        myRequired.validate( "foo" ) shouldBe valid( "foo" )
        myRequired.validate( "  foo   " ) shouldBe valid( "foo" )
        myRequired.validate( "" ) shouldBe invalidNel( "Required" )
        myRequired.validate( "    " ) shouldBe invalidNel( "Required" )
    }

    it should "be possible to override Operation error messages" in {
        class MyRequired extends ( IsDefined[String] <*> Required )

        object MyRequired {
            implicit val error = Error.instance[MyRequired]( "foobar" )
        }

        val myRequired = new MyRequired

        myRequired.validate( Some( "foobar" ) ) shouldBe valid( "foobar" )
        myRequired.validate( Some( "" ) ) shouldBe invalidNel( "foobar" )
        myRequired.validate( None ) shouldBe invalidNel( "foobar" )

        class MyMatch extends ( Trim <~> Matches[Witness.`"foo"`.T, String] )

        object MyMatch {
            implicit val error = Error.instance[MyMatch]( "foobar" )
        }

        val myMatch = new MyMatch

        myMatch.validate( "foo" ) shouldBe valid( "foo" )
        myMatch.validate( " foo  " ) shouldBe valid( "foo" )
        myMatch.validate( "" ) shouldBe invalidNel( "foobar" )
    }
}