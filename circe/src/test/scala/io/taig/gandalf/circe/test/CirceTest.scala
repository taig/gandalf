package io.taig.gandalf.circe.test

import cats.data.Validated.Invalid
import io.circe.Json
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import io.taig.gandalf.circe._
import io.taig.gandalf.core.test.Suite
import io.taig.gandalf.macros.obeys
import io.taig.gandalf.predef.string.required
import io.taig.gandalf.predef.numeric.gt

import scala.util.Left

class CirceTest extends Suite {
    case class A( b: B, c: C )
    case class B( @obeys( required ) value:String )
    case class C( @obeys( gt( 5 ) ) value:Int )

    it should "serialize ParsingFailures" in {
        val raw = """{"b":{"value":12_34},"c":{"value":10}}"""
        val Left( failure ) = parse( raw )

        failure.asJson shouldBe "expected } or , got _ (line 1, column 17)".asJson
    }

    it should "serialize DecodingFailures" in {
        val raw = """{"b":{"value":""},"c":{"value":10}}"""
        val Left( failure ) = decode[A]( raw )

        failure.asJson shouldBe Json.obj(
            "b" → Json.obj(
                "value" → List( "not(empty)" ).asJson
            )
        )
    }

    it should "allow to accumulate validation errors" in {
        val raw = """{"b":{"value":""},"c":{"value":1}}"""
        val Invalid( failures ) = decodeAccumulating[A]( raw )

        failures.asJson shouldBe Json.obj(
            "b" → Json.obj(
                "value" → List( "not(empty)" ).asJson
            ),
            "c" → Json.obj(
                "value" → List( "gt" ).asJson
            )
        )
    }
}