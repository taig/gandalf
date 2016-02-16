package io.taig.bsts.tests

import cats.data.NonEmptyList
import cats.data.Xor._
import io.taig.bsts.implicits._
import shapeless.test.illTyped

class RawTest extends Suite {
    it should "be available for Errors" in {
        rule.required.validate( "" ) match {
            case Right( _ )    ⇒ fail()
            case Left( error ) ⇒ error.raw shouldBe ( "required", Nil )
        }
    }

    it should "be available for Terms" in {
        rule.required.validate( "" ).raw shouldBe Left( ( "required", List.empty[Any] ) )
    }

    it should "be available for Policies" in {
        ( rule.required ~> mutation.parse ).validate( "asdf" ).raw shouldBe
            Left( NonEmptyList( ( "parse", List.empty[Any] ) ) )

        ( transformation.trim ~> rule.required ).validate( "  " ).raw shouldBe
            Left( NonEmptyList( ( "required", List.empty[Any] ) ) )
    }

    it should "not be available for Transformations" in {
        illTyped {
            "transformation.trim.validate( \"asdf\" ).raw"
        }
    }
}