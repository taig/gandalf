package io.taig.bsts.tests

import cats.data.NonEmptyList
import cats.data.Validated.{ Invalid, Valid }
import io.taig.bsts.implicits._
import io.taig.bsts.report.syntax.term._

class RawTest extends Suite {
    it should "be available for Errors" in {
        rule.required.validate( "" ) match {
            case Valid( _ )       ⇒ fail()
            case Invalid( error ) ⇒ error.raw shouldBe NonEmptyList( ( "required", Nil ) )
        }
    }

    it should "be available for Validations" in {
        rule.required.validate( "" ).raw shouldBe Invalid( NonEmptyList( ( "required", Nil ) ) )
    }

    it should "be available for Policies" in {
        ( rule.required ~> mutation.parse ).validate( "asdf" ).raw shouldBe
            Invalid( NonEmptyList( ( "parse", Nil ) ) )

        ( transformation.trim ~> rule.required ).validate( "  " ).raw shouldBe
            Invalid( NonEmptyList( ( "required", Nil ) ) )
    }

    it should "be available for ReportableErrors" in {
        rule.required.as0( "yolo" ).validate( "" ).raw
    }
}