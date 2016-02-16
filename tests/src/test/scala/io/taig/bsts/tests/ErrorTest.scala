package io.taig.bsts.tests

import cats.data.Xor._

class ErrorTest extends Suite {
    it should "have a toString representation" in {
        rule.required.validate( "" ) match {
            case Right( _ )    ⇒ fail()
            case Left( error ) ⇒ error.toString shouldBe "Error(required)"
        }

        rule.max( 3 ).validate( "foobar" ) match {
            case Right( _ )    ⇒ fail()
            case Left( error ) ⇒ error.toString shouldBe "Error(max, (foobar, 3, 6))"
        }
    }
}