package io.taig.gandalf.tests

import cats.data.Validated
import cats.data.Validated.{ Invalid, Valid }

class ErrorTest extends Suite {
    it should "have a toString representation" in {
        rule.required.validate( "" ) match {
            case Valid( _ )       ⇒ fail()
            case Invalid( error ) ⇒ error.toString shouldBe "Error(required)"
        }

        rule.max( 3 ).validate( "foobar" ) match {
            case Valid( _ )       ⇒ fail()
            case Invalid( error ) ⇒ error.toString shouldBe "Error(max, (foobar, 3, 6))"
        }
    }
}