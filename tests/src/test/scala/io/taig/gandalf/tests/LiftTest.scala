package io.taig.gandalf.tests

import cats.data.Validated._
import io.taig.gandalf.predef._
import io.taig.gandalf.predef.messages._
import io.taig.gandalf.syntax.all._
import shapeless.test.illTyped

class LiftTest extends Suite {
    "tryLift" should "perform runtime validations" in {
        tryLift[Required]( "foobar" ) shouldBe valid( "foobar" )
        tryLift[Required]( "" ) shouldBe invalidNel( "Required" )

        tryLift[Trim <~> Required]( "foobar" ) shouldBe valid( "foobar" )
        tryLift[Trim <~> Required]( "  foobar   " ) shouldBe valid( "foobar" )
        tryLift[Trim <~> Required]( "    " ) shouldBe invalidNel( "Required" )
        tryLift[Trim <~> Required]( "" ) shouldBe invalidNel( "Required" )
    }

    "lift" should "perform compile time validations" in {
        lift[Required]( "foobar" ).value shouldBe "foobar"
        assertTypeError( """lift[Required]( "" )""" )

        lift[Trim <~> Required]( "foobar" ).value shouldBe "foobar"
        lift[Trim <~> Required]( "   foobar    " ).value shouldBe "foobar"
        assertTypeError( """lift[Trim <~> Required]( "   " )""" )
        assertTypeError( """lift[Trim <~> Required]( "" )""" )
    }
}