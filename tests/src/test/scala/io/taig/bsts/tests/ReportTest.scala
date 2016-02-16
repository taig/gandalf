package io.taig.bsts.tests

import cats.data.NonEmptyList
import cats.data.Xor._
import io.taig.bsts._
import io.taig.bsts.report.syntax.report._
import io.taig.bsts.report.{ Report, ReportableTerm }
import io.taig.bsts.syntax.dsl._
import shapeless.HNil
import shapeless.record._
import shapeless.test.illTyped

class ReportTest extends Suite {
    object report {
        implicit val required = Report( rule.required )( _ ⇒ "Pflichtfeld" )

        implicit val min = Report( rule.min _ )( args ⇒ s"Mindestens ${args( "expected" )} Zeichen" )

        implicit val parse = Report( mutation.parse )( _ ⇒ "Keine gültige Zahl" )
    }

    it should "be available for Errors" in {
        import report._

        rule.required.validate( "" ) match {
            case Right( _ )    ⇒ fail()
            case Left( error ) ⇒ error.report shouldBe "Pflichtfeld"
        }

        mutation.parse.validate( "foo" ) match {
            case Right( _ )    ⇒ fail()
            case Left( error ) ⇒ error.report shouldBe "Keine gültige Zahl"
        }
    }

    it should "be available for Terms" in {
        import report._

        rule.required.validate( "" ).report shouldBe Left( "Pflichtfeld" )

        mutation.parse.validate( "foo" ).report shouldBe Left( "Keine gültige Zahl" )
    }

    it should "be available for Policies" in {
        import report._

        Policy( rule.required :: HNil ).validate( "" ).report shouldBe Left( NonEmptyList( "Pflichtfeld" ) )
        Policy( rule.min( 6 ) :: HNil ).validate( "" ).report shouldBe Left( NonEmptyList( "Mindestens 6 Zeichen" ) )

        ( rule.required & rule.min( 6 ) ).validate( "foo" ).report shouldBe
            Left( NonEmptyList( "Mindestens 6 Zeichen" ) )

        ( transformation.trim ~> rule.min( 6 ) ).validate( "foo     " ).report shouldBe
            Left( NonEmptyList( "Mindestens 6 Zeichen" ) )
    }

    it should "not be available for Transformations" in {
        illTyped {
            "transformation.trim.validate( \"asdf\" ).report"
        }
    }

    it should "be possible to attach reports to Terms" in {
        rule.required.as( _ ⇒ "yolo" ) shouldBe a[ReportableTerm[_, _, _, _]]
    }

    it should "be possible to report a ReportableError" in {
        rule.required.as( _ ⇒ "yolo" ).validate( "" ) match {
            case Right( _ )    ⇒ fail()
            case Left( error ) ⇒ error.report shouldBe "yolo"
        }
    }

    it should "be possible to report a Rightated[ReportableError]" in {
        rule.required.as( _ ⇒ "yolo" ).validate( "" ).report shouldBe Left( "yolo" )
    }
}