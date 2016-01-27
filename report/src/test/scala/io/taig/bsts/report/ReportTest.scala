package io.taig.bsts.report

import io.taig.bsts._
import io.taig.bsts.report.syntax.report._
import shapeless.record._

class ReportTest extends Suite {
    object report {
        implicit val required = Report( rule.required )( "Pflichtfeld" )

        implicit val min = Report( rule.min _ ) as { args ⇒ s"Mindestens ${args( "expected" )} Zeichen" }
    }

    it should "be available for Errors" in {
        import report.required

        rule.required.validate( "" ) match {
            case Success( _ )     ⇒ fail()
            case Failure( error ) ⇒ error.report shouldBe "Pflichtfeld"
        }
    }

    it should "be available for Rule Failures" in {
        import report.required

        rule.required.validate( "" ) match {
            case Success( _ )         ⇒ fail()
            case f @ Failure( error ) ⇒ f.report shouldBe error.report
        }
    }

    it should "be available for Policy Failures" in {
        import report._

        Policy( rule.required ).validate( "" ) match {
            case Success( _ )     ⇒ fail()
            case f @ Failure( _ ) ⇒ f.report shouldBe List( "Pflichtfeld" )
        }

        ( rule.required & rule.min( 6 ) ).validate( "foo" ) match {
            case Success( _ )     ⇒ fail()
            case f @ Failure( _ ) ⇒ f.report shouldBe List( "Mindestens 6 Zeichen" )
        }
    }
}