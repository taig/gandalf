package io.taig.gandalf.circe

import cats.data.Validated.Invalid
import io.circe.Json
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import io.taig.gandalf.Suite
import io.taig.gandalf.macros.obeys
import io.taig.gandalf.predef._
import io.taig.gandalf.report.Report

import scala.util.Left

class CirceTest extends Suite {
    case class A( b: B, c: C )
    case class B( @obeys( required ) value:String )
    case class C( @obeys( gt( 5 ) ) value:Int )

    implicit def reportRequired[T]: Report[required, T, T] =
        Report.static( "required" )

    implicit def reportGt[V: ValueOf, T]: Report[gt[V], T, T] =
        Report.static( s"greater than ${valueOf[V]}" )

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
                "value" → List( "required" ).asJson
            )
        )
    }

    it should "allow to accumulate rule errors" in {
        val raw = """{"b":{"value":""},"c":{"value":1}}"""
        val Invalid( failures ) = decodeAccumulating[A]( raw )

        failures.asJson shouldBe Json.obj(
            "b" → Json.obj(
                "value" → List( "required" ).asJson
            ),
            "c" → Json.obj(
                "value" → List( "greater than 5" ).asJson
            )
        )
    }
}