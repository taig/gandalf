package io.taig.gandalf.tests

import cats.data.Validated._
import io.taig.gandalf._
import io.taig.gandalf.data.Mutate
import io.taig.gandalf.predef._
import io.taig.gandalf.predef.messages._
import io.taig.gandalf.syntax.all._

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
        object MyRequired extends MyRequired

        //        implicit val error = Error.instance[IsDefined[String] <*> Required]( "foobar" )
        implicit val error = Error.instance[MyRequired]( "foobar" )

        MyRequired.validate( Some( "" ) )( Mutate.validation ) shouldBe invalidNel( "foobar" )
    }
}