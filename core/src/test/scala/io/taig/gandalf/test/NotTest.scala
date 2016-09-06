package io.taig.gandalf.test

import cats.data.NonEmptyList
import cats.data.Validated._
import io.taig.gandalf.syntax.all._
import io.taig.gandalf.{ not ⇒ dont, _ }
import shapeless.test.illTyped

class NotTest extends Suite {
    it should "fail validation when the underlying Condition succeeds" in {
        dont( condition.success ).validate( "foo" ) shouldBe
            invalidNel( "success" )
    }

    it should "succeed validation when the underlying Condition fails" in {
        dont( condition.failure ).validate( "foo" ) shouldBe
            valid( "foo" )
    }

    it should "support combined Conditions" in {
        dont( condition.success && condition.failure ).validate( "foo" ) shouldBe
            valid( "foo" )
        dont( condition.success && condition.success ).validate( "foo" ) shouldBe
            invalid( NonEmptyList.of( "success", "success" ) )
    }

    it should "support custom Conditions" in {
        object failure extends ( condition.success.type & condition.failure.type )
        dont( failure ).validate( "foo" ) shouldBe valid( "foo" )

        object success extends ( condition.success.type || condition.failure.type )
        dont( success ).validate( "foo" ) shouldBe
            invalid( NonEmptyList.of( "success", "condition" ) )
    }

    it should "support custom Conditions with custom Errors" in {
        object success extends ( condition.success.type || condition.failure.type ) {
            implicit val error: Error[this.type] = Error.instance( _ ⇒ "custom" )
        }

        dont( success ).validate( "foo" ) shouldBe
            invalid( NonEmptyList.of( "custom" ) )
    }

    it should "only work with Conditions" in {
        dont( condition.success )

        illTyped( "dont( mutation.success )" )
        illTyped( "dont( transformation.success )" )
    }
}