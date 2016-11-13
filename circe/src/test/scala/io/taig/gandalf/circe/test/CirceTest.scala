package io.taig.gandalf.circe.test

import io.circe.Json
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import io.taig.gandalf.circe._
import io.taig.gandalf.core.test.Suite
import io.taig.gandalf.macros.obeys
import io.taig.gandalf.predef.string.required

class CirceTest extends Suite {
    it should "serialize DecodingFailures" in {
        case class B( @obeys( required ) b:String )
        case class A( a: B )

        val raw = """{"a":{"b":""}}"""
        val Right( json ) = parse( raw )
        val Left( failure ) = json.as[A]

        failure.asJson shouldBe Json.obj(
            "a" → Json.obj(
                "b" → List( "not(isEmpty)" ).asJson
            )
        )
    }
}