package io.taig.gandalf.core.test

import cats.data.NonEmptyList
import cats.data.Validated._
import io.taig.gandalf.core.syntax.all._
import io.taig.gandalf.core.{ not ⇒ dont, _ }

class NotTest extends Suite {
    it should "fail validation when the underlying Condition succeeds" in {
        dont( condition.success ).validate( "foo" ) shouldBe
            invalidNel( "not(success)" )
    }

    it should "succeed validation when the underlying Condition fails" in {
        dont( condition.failure ).validate( "foo" ) shouldBe
            valid( "foo" )
    }

    it should "not support Mutations" in {
        assertTypeError( """dont( mutation.success ).validate( "foo" )""" )
        assertTypeError( """dont( mutation.failure ).validate( "foo" )""" )
    }

    it should "not support Transformations" in {
        assertTypeError( """dont( transformation ).validate( "foo" )""" )
    }

    it should "even out when applied twice" in {
        dont( dont( condition.success ) ).validate( "foo" ) shouldBe
            valid( "foo" )
        dont( dont( condition.failure ) ).validate( "foo" ) shouldBe
            invalidNel( "condition" )
    }

    it should "support not-specific Errors" in {
        implicit val error: Error[dont[condition.success.type]] = {
            Error.static( "failure" )
        }

        dont( condition.success ).validate( "foo" ) shouldBe
            invalidNel( "failure" )
    }

    "&&" should "support Conditions" in {
        dont( condition.success && condition.failure ).validate( "foo" ) shouldBe
            valid( "foo" )
        dont( condition.success && condition.success ).validate( "foo" ) shouldBe
            invalid( NonEmptyList.of( "not(success)", "not(success)" ) )
    }

    it should "support custom Conditions" in {
        object success extends ( condition.success.type && condition.success.type )
        dont( success ).validate( "foo" ) shouldBe
            invalid( NonEmptyList.of( "not(success)", "not(success)" ) )

        object failure extends ( condition.success.type && condition.failure.type )
        dont( failure ).validate( "foo" ) shouldBe valid( "foo" )
    }

    it should "support Mutations with Conditions" in {
        dont( condition.success && mutation.success ).validate( "foo" ) shouldBe
            invalidNel( "not(success)" )
        dont( condition.failure && mutation.success ).validate( "foo" ) shouldBe
            valid( "foo" )
        dont( mutation.success && condition.success ).validate( "foo" ) shouldBe
            invalidNel( "not(success)" )
        dont( mutation.success && condition.failure ).validate( "foo" ) shouldBe
            valid( "foo" )
        dont( condition.success && mutation.failure ).validate( "foo" ) shouldBe
            invalidNel( "not(success)" )
        dont( condition.failure && mutation.failure ).validate( "foo" ) shouldBe
            invalidNel( "mutation" )
        dont( mutation.failure && condition.success ).validate( "foo" ) shouldBe
            invalidNel( "mutation" )
        dont( mutation.failure && condition.failure ).validate( "foo" ) shouldBe
            invalidNel( "mutation" )
    }

    it should "support Transformations with Conditions" in {
        dont( condition.success && transformation ).validate( "foo" ) shouldBe
            invalidNel( "not(success)" )
        dont( condition.failure && transformation ).validate( "foo" ) shouldBe
            valid( "foo" )
        dont( transformation && condition.success ).validate( "foo" ) shouldBe
            invalidNel( "not(success)" )
        dont( transformation && condition.failure ).validate( "foo" ) shouldBe
            valid( "foo" )
    }

    it should "support custom compositions with custom Errors" in {
        object success extends ( condition.success.type && condition.success.type ) {
            implicit val error: Error[this.type] = Error.static( "custom" )
        }

        dont( success ).validate( "foo" ) shouldBe invalidNel( "not(custom)" )
    }

    it should "support nesting" in {
        dont( condition.success && dont( condition.failure ) ).validate( "foo" ) shouldBe
            invalid( NonEmptyList.of( "not(success)", "condition" ) )
        dont( condition.success && dont( condition.success ) ).validate( "foo" ) shouldBe
            valid( "foo" )
        dont( condition.failure && dont( condition.failure ) ).validate( "foo" ) shouldBe
            valid( "foo" )
        dont( condition.failure && dont( condition.success ) ).validate( "foo" ) shouldBe
            valid( "foo" )
    }

    "&" should "support Conditions" in {
        dont( condition.success & condition.failure ).validate( "foo" ) shouldBe
            valid( "foo" )
        dont( condition.success & condition.success ).validate( "foo" ) shouldBe
            invalid( NonEmptyList.of( "not(success)", "not(success)" ) )
    }

    it should "support custom Conditions" in {
        object success extends ( condition.success.type & condition.success.type )
        dont( success ).validate( "foo" ) shouldBe
            invalid( NonEmptyList.of( "not(success)", "not(success)" ) )

        object failure extends ( condition.success.type & condition.failure.type )
        dont( failure ).validate( "foo" ) shouldBe valid( "foo" )
    }

    it should "support Mutations with Conditions" in {
        dont( condition.success & mutation.success ).validate( "foo" ) shouldBe
            invalidNel( "not(success)" )
        dont( condition.failure & mutation.success ).validate( "foo" ) shouldBe
            valid( "foo" )
        dont( mutation.success & condition.success ).validate( "foo" ) shouldBe
            invalidNel( "not(success)" )
        dont( mutation.success & condition.failure ).validate( "foo" ) shouldBe
            valid( "foo" )
        dont( condition.success & mutation.failure ).validate( "foo" ) shouldBe
            invalid( NonEmptyList.of( "not(success)", "mutation" ) )
        dont( condition.failure & mutation.failure ).validate( "foo" ) shouldBe
            invalidNel( "mutation" )
        dont( mutation.failure & condition.success ).validate( "foo" ) shouldBe
            invalid( NonEmptyList.of( "mutation", "not(success)" ) )
        dont( mutation.failure & condition.failure ).validate( "foo" ) shouldBe
            invalidNel( "mutation" )
    }

    it should "support Transformations with Conditions" in {
        dont( condition.success & transformation ).validate( "foo" ) shouldBe
            invalidNel( "not(success)" )
        dont( condition.failure & transformation ).validate( "foo" ) shouldBe
            valid( "foo" )
        dont( transformation & condition.success ).validate( "foo" ) shouldBe
            invalidNel( "not(success)" )
        dont( transformation & condition.failure ).validate( "foo" ) shouldBe
            valid( "foo" )
    }

    it should "support custom compositions with custom Errors" in {
        object success extends ( condition.success.type & condition.success.type ) {
            implicit val error: Error[this.type] = Error.static( "custom" )
        }

        dont( success ).validate( "foo" ) shouldBe invalidNel( "not(custom)" )
    }

    it should "support nesting" in {
        dont( condition.success & dont( condition.failure ) ).validate( "foo" ) shouldBe
            invalid( NonEmptyList.of( "not(success)", "condition" ) )
        dont( condition.success & dont( condition.success ) ).validate( "foo" ) shouldBe
            valid( "foo" )
        dont( condition.failure & dont( condition.failure ) ).validate( "foo" ) shouldBe
            valid( "foo" )
        dont( condition.failure & dont( condition.success ) ).validate( "foo" ) shouldBe
            valid( "foo" )
    }

    "||" should "support Conditions" in {
        dont( condition.success || condition.failure ).validate( "foo" ) shouldBe
            invalidNel( "not(success)" )
        dont( condition.failure || condition.failure ).validate( "foo" ) shouldBe
            valid( "foo" )
    }

    it should "support custom Conditions" in {
        object success extends ( condition.success.type || condition.success.type )
        dont( success ).validate( "foo" ) shouldBe
            invalid( NonEmptyList.of( "not(success)", "not(success)" ) )

        object failure extends ( condition.failure.type || condition.failure.type )
        dont( failure ).validate( "foo" ) shouldBe valid( "foo" )
    }

    it should "support Mutations with Conditions" in {
        dont( condition.success || mutation.success ).validate( "foo" ) shouldBe
            valid( "foo" )
        dont( condition.failure || mutation.success ).validate( "foo" ) shouldBe
            valid( "foo" )
        dont( mutation.success || condition.success ).validate( "foo" ) shouldBe
            valid( "foo" )
        dont( mutation.success || condition.failure ).validate( "foo" ) shouldBe
            valid( "foo" )
        dont( condition.success || mutation.failure ).validate( "foo" ) shouldBe
            invalid( NonEmptyList.of( "not(success)", "mutation" ) )
        dont( condition.failure || mutation.failure ).validate( "foo" ) shouldBe
            valid( "foo" )
        dont( mutation.failure || condition.success ).validate( "foo" ) shouldBe
            invalid( NonEmptyList.of( "mutation", "not(success)" ) )
        dont( mutation.failure || condition.failure ).validate( "foo" ) shouldBe
            valid( "foo" )
    }

    it should "support Transformations with Conditions" in {
        dont( condition.success || transformation ).validate( "foo" ) shouldBe
            valid( "foo" )
        dont( condition.failure || transformation ).validate( "foo" ) shouldBe
            valid( "foo" )
        dont( transformation || condition.success ).validate( "foo" ) shouldBe
            valid( "foo" )
        dont( transformation || condition.failure ).validate( "foo" ) shouldBe
            valid( "foo" )
    }

    it should "support custom compositions with custom Errors" in {
        object success extends ( condition.success.type || condition.failure.type ) {
            implicit val error: Error[this.type] = Error.static( "custom" )
        }

        dont( success ).validate( "foo" ) shouldBe invalidNel( "not(custom)" )
    }

    it should "support nesting" in {
        dont( condition.success || dont( condition.failure ) ).validate( "foo" ) shouldBe
            invalid( NonEmptyList.of( "not(success)", "condition" ) )
        dont( condition.success || dont( condition.success ) ).validate( "foo" ) shouldBe
            invalidNel( "not(success)" )
        dont( condition.failure || dont( condition.failure ) ).validate( "foo" ) shouldBe
            invalidNel( "condition" )
        dont( condition.failure || dont( condition.success ) ).validate( "foo" ) shouldBe
            valid( "foo" )
    }
}