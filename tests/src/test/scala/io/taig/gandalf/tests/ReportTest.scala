package io.taig.gandalf.tests

import cats.data.NonEmptyList
import cats.data.Validated.{ Invalid, Valid }
import io.taig.gandalf._
import io.taig.gandalf.report.ReportableTerm
import io.taig.gandalf.report.syntax.report._
import io.taig.gandalf.report.syntax.term._
import io.taig.gandalf.syntax.dsl._
import shapeless.HNil
import shapeless.test.illTyped

class ReportTest extends Suite {
    it should "be available for Errors" in {
        import report._

        rule.required.validate( "" ) match {
            case Valid( _ )       ⇒ fail()
            case Invalid( error ) ⇒ error.report shouldBe NonEmptyList.of( "Pflichtfeld" )
        }

        mutation.parse.validate( "foo" ) match {
            case Valid( _ )       ⇒ fail()
            case Invalid( error ) ⇒ error.report shouldBe NonEmptyList.of( "Keine gültige Zahl" )
        }
    }

    it should "be available for Terms" in {
        import report._

        rule.required.validate( "" ).report shouldBe Invalid( NonEmptyList.of( "Pflichtfeld" ) )

        mutation.parse.validate( "foo" ).report shouldBe Invalid( NonEmptyList.of( "Keine gültige Zahl" ) )
    }

    it should "be available for Policies" in {
        import report._

        Policy( rule.required :: HNil ).validate( "" ).report[String] shouldBe
            Invalid( NonEmptyList.of( "Pflichtfeld" ) )
        Policy( rule.min( 6 ) :: HNil ).validate( "" ).report[String] shouldBe
            Invalid( NonEmptyList.of( "Mindestens 6 Zeichen" ) )

        ( rule.required & rule.min( 6 ) ).validate( "foo" ).report[String] shouldBe
            Invalid( NonEmptyList.of( "Mindestens 6 Zeichen" ) )

        ( transformation.trim ~> rule.min( 6 ) ).validate( "foo     " ).report[String] shouldBe
            Invalid( NonEmptyList.of( "Mindestens 6 Zeichen" ) )
    }

    it should "be possible to attach reports to Terms" in {
        rule.required.as( _ ⇒ "yolo" ) shouldBe a[ReportableTerm[_, _, _, _, _]]
        rule.required.as0( "yolo" ) shouldBe a[ReportableTerm[_, _, _, _, _]]
    }

    it should "be possible to attach reports to Terms in Policies" in {
        import report._

        Policy( rule.required.as0( "yolo" ) :: HNil ).validate( "" ).report[String] shouldBe
            Invalid( NonEmptyList.of( "yolo" ) )

        ( rule.required & rule.required.as0( "yolo" ) ).validate( "" ).report[String] shouldBe
            Invalid( NonEmptyList.of( "Pflichtfeld", "yolo" ) )
    }

    it should "be possible to report a ReportableError" in {
        rule.required.as0( "yolo" ).validate( "" ) match {
            case Valid( _ )       ⇒ fail()
            case Invalid( error ) ⇒ error.report shouldBe NonEmptyList.of( "yolo" )
        }
    }
}