package io.taig.gandalf.test

import cats.data.NonEmptyList
import cats.data.Validated._
import io.taig.gandalf.syntax.all._
import io.taig.gandalf.{ not ⇒ dont, _ }

class NotTest extends Suite {
    it should "fail validation when the underlying Condition succeeds" in {
        dont( condition.success ).validate( "foo" ) shouldBe
            invalidNel( "not(success)" )
    }

    it should "succeed validation when the underlying Condition fails" in {
        dont( condition.failure ).validate( "foo" ) shouldBe
            valid( "foo" )
    }

    it should "support combined Conditions" in {
        dont( condition.success && condition.failure ).validate( "foo" ) shouldBe
            valid( "foo" )
        dont( condition.success && condition.success ).validate( "foo" ) shouldBe
            invalid( NonEmptyList.of( "not(success)", "not(success)" ) )
    }

    it should "support custom Conditions" in {
        object failure extends ( condition.success.type & condition.failure.type )
        dont( failure ).validate( "foo" ) shouldBe valid( "foo" )

        object success extends ( condition.success.type || condition.failure.type )
        dont( success ).validate( "foo" ) shouldBe
            invalid( NonEmptyList.of( "not(success)", "not(condition)" ) )
    }

    it should "support custom Conditions with custom Errors" in {
        object success extends ( condition.success.type || condition.failure.type ) {
            implicit val error: Error[this.type] = Error.one( _ ⇒ "custom" )
        }

        dont( success ).validate( "foo" ) shouldBe
            invalid( NonEmptyList.of( "not(custom)" ) )
    }

    it should "support not-specific Errors" in {
        implicit val error: Error[dont[condition.success.type]] = {
            Error.one( _ ⇒ "failure" )
        }

        dont( condition.success ).validate( "foo" ) shouldBe
            invalidNel( "failure" )
    }
}