package io.taig.gandalf.tests

import cats.data.Validated._
import io.taig.gandalf.predef._
import io.taig.gandalf.predef.messages._
import io.taig.gandalf.syntax.all._

class LiftTest extends Suite {
    "tryLift" should "allow runtime validation checks" in {
        tryLift[Required]( "foobar" ) shouldBe valid( "foobar" )
        tryLift[Required]( "" ) shouldBe invalidNel( "Required" )

        tryLift[Trim <~> Required]( "foobar" ) shouldBe valid( "foobar" )
        tryLift[Trim <~> Required]( "  foobar   " ) shouldBe valid( "foobar" )
        tryLift[Trim <~> Required]( "    " ) shouldBe invalidNel( "Required" )
        tryLift[Trim <~> Required]( "" ) shouldBe invalidNel( "Required" )
    }
}