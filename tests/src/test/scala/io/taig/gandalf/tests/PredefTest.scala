package io.taig.gandalf.tests

import cats.data.Validated._
import io.taig.gandalf.implicits._
import io.taig.gandalf.predef.IsDefined.isDefined
import io.taig.gandalf.predef.Matches.matches

class PredefTest extends Suite {
    "IsDefined" should "validate Options" in {
        isDefined[String].validate( Some( "foo" ) ) shouldBe valid( "foo" )
        isDefined[String].validate( None ) shouldBe invalid( List( "isDefined" ) )
    }

    "Matches" should "check for equality" in {
    }
}