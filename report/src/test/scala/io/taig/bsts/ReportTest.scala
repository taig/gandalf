package io.taig.bsts

import io.taig.bsts.syntax.all._
import shapeless.record._

class ReportTest extends Suite {
    object report {
        implicit val required = Report( rule.required )( "Pflichtfeld" )

        implicit val min = Report( rule.min _ ) as { args ⇒ s"Mindestens ${args( "expected" )} Zeichen" }
    }

    "report" should "be available for Errors" in {
        import report.required

        rule.required.validate( "" ) match {
            case Success( _ )     ⇒ fail()
            case Failure( error ) ⇒ error.report shouldBe "Pflichtfeld"
        }
    }

    it should "be available for Rule Failures" in {
        import report.required

        rule.required.validate( "" ) match {
            case Success( _ )     ⇒ fail()
            case f @ Failure( _ ) ⇒ f.report shouldBe "Pflichtfeld"
        }
    }

    it should "be available for Policy Failures" in {
        import report.required

        Policy( rule.required ).validate( "" ) match {
            case Success( _ )     ⇒ fail()
            case f @ Failure( _ ) ⇒ f.report shouldBe List( "Pflichtfeld" )
        }
    }
}