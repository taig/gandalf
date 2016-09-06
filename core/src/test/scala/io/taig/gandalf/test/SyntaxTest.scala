package io.taig.gandalf.test

import cats.data.NonEmptyList
import cats.data.Validated._
import io.taig.gandalf.syntax.all._
import shapeless.test.illTyped

class SyntaxTest extends Suite {
    "~>" should "combine Mutations" in {
        ( mutation.success ~> mutation.success ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( mutation.success ~> mutation.failure ).validate( "foo" ) shouldBe
            invalidNel( "mutation" )
        ( mutation.failure ~> mutation.success ).validate( "foo" ) shouldBe
            invalidNel( "mutation" )
        ( mutation.failure ~> mutation.failure ).validate( "foo" ) shouldBe
            invalidNel( "mutation" )
    }

    it should "combine Transformations" in {
        ( transformation ~> transformation ).validate( "foo" ) shouldBe
            valid( "foo" )
    }

    it should "combine Mutations with Transformations" in {
        ( mutation.success ~> transformation ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( mutation.failure ~> transformation ).validate( "foo" ) shouldBe
            invalidNel( "mutation" )
        ( transformation ~> mutation.success ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( transformation ~> mutation.failure ).validate( "foo" ) shouldBe
            invalidNel( "mutation" )
    }

    it should "combine with Conditions on the rhs" in {
        ( mutation.success ~> condition.success ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( mutation.success ~> condition.failure ).validate( "foo" ) shouldBe
            invalidNel( "condition" )
        ( mutation.failure ~> condition.success ).validate( "foo" ) shouldBe
            invalidNel( "mutation" )
        ( mutation.failure ~> condition.failure ).validate( "foo" ) shouldBe
            invalidNel( "mutation" )
    }

    it should "not combine with Conditions on the lhs" in {
        illTyped( "condition.success ~> mutation.success" )
    }

    "&&" should "combine Conditions" in {
        ( condition.success && condition.success ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( condition.success && condition.failure ).validate( "foo" ) shouldBe
            invalidNel( "condition" )
        ( condition.failure && condition.success ).validate( "foo" ) shouldBe
            invalidNel( "condition" )
        ( condition.failure && condition.failure ).validate( "foo" ) shouldBe
            invalidNel( "condition" )
    }

    it should "not combine with Mutations" in {
        illTyped( "condition.success && mutation.success" )
        illTyped( "mutation.success && condition.success" )
    }

    "&" should "combine Conditions" in {
        ( condition.success & condition.success ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( condition.success & condition.failure ).validate( "foo" ) shouldBe
            invalidNel( "condition" )
        ( condition.failure & condition.success ).validate( "foo" ) shouldBe
            invalidNel( "condition" )
        ( condition.failure & condition.failure ).validate( "foo" ) shouldBe
            invalid ( NonEmptyList.of( "condition", "condition" ) )
    }

    it should "not combine with Mutations" in {
        illTyped( "condition.success & mutation.success" )
        illTyped( "mutation.success & condition.success" )
    }

    "||" should "combine Conditions" in {
        ( condition.success || condition.success ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( condition.success || condition.failure ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( condition.failure || condition.success ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( condition.failure || condition.failure ).validate( "foo" ) shouldBe
            invalid( NonEmptyList.of( "condition", "condition" ) )
    }

    it should "not combine with Mutations" in {
        illTyped( "condition.success || mutation.success" )
        illTyped( "mutation.success || condition.success" )
    }
}