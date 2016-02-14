package io.taig.bsts.tests

import io.taig.bsts._
import io.taig.bsts.data.NonEmptyList
import io.taig.bsts.data.Validated.{ Valid, Invalid }
import io.taig.bsts.report.Report
import io.taig.bsts.syntax.dsl._
import io.taig.bsts.report.syntax.report._
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
            case Valid( _ )       ⇒ fail()
            case Invalid( error ) ⇒ error.report shouldBe "Pflichtfeld"
        }

        mutation.parse.validate( "foo" ) match {
            case Valid( _ )       ⇒ fail()
            case Invalid( error ) ⇒ error.report shouldBe "Keine gültige Zahl"
        }
    }

    it should "be available for Terms" in {
        import report._

        rule.required.validate( "" ).report[String] shouldBe Invalid( "Pflichtfeld" )

        mutation.parse.validate( "foo" ).report[String] shouldBe Invalid( "Keine gültige Zahl" )
    }

    it should "be available for Policies" in {
        import report._

        Policy( rule.required :: HNil ).validate( "" ).report[String] shouldBe Invalid( NonEmptyList( "Pflichtfeld" ) )
        Policy( rule.min( 6 ) :: HNil ).validate( "" ).report[String] shouldBe Invalid( NonEmptyList( "Mindestens 6 Zeichen" ) )

        ( rule.required & rule.min( 6 ) ).validate( "foo" ).report[String] shouldBe
            Invalid( NonEmptyList( "Mindestens 6 Zeichen" ) )

        ( transformation.trim ~> rule.min( 6 ) ).validate( "foo     " ).report[String] shouldBe
            Invalid( NonEmptyList( "Mindestens 6 Zeichen" ) )
    }

    it should "not be available for Transformations" in {
        illTyped {
            "transformation.trim.validate( \"asdf\" ).report[String]"
        }
    }
}