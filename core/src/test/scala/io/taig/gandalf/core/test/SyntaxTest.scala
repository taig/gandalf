package io.taig.gandalf.core.test

import cats.data.NonEmptyList
import cats.data.Validated._
import io.taig.gandalf.core.{ Arguments, Mutation, Transformation }
import io.taig.gandalf.core.syntax.all._

class SyntaxTest extends Suite {
    it should "provide a toString representation" in {
        ( condition.success && condition.failure ).serialize shouldBe
            "(success && failure)"
        ( condition.success & condition.failure ).serialize shouldBe
            "(success & failure)"
        ( condition.success || condition.failure ).serialize shouldBe
            "(success || failure)"
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

    it should "combine Mutations" in {
        ( mutation.success && mutation.success ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( mutation.success && mutation.failure ).validate( "foo" ) shouldBe
            invalidNel( "mutation" )
        ( mutation.failure && mutation.success ).validate( "foo" ) shouldBe
            invalidNel( "mutation" )
        ( mutation.failure && mutation.failure ).validate( "foo" ) shouldBe
            invalidNel( "mutation" )
    }

    it should "combine Transformations" in {
        ( transformation && transformation ).validate( "foo" ) shouldBe
            valid( "foo" )
    }

    it should "combine Mutations with Conditions" in {
        ( mutation.success && condition.success ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( mutation.failure && condition.success ).validate( "foo" ) shouldBe
            invalidNel( "mutation" )
        ( mutation.success && condition.failure ).validate( "foo" ) shouldBe
            invalidNel( "condition" )
        ( mutation.failure && condition.failure ).validate( "foo" ) shouldBe
            invalidNel( "mutation" )
        ( condition.success && mutation.success ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( condition.success && mutation.failure ).validate( "foo" ) shouldBe
            invalidNel( "mutation" )
        ( condition.failure && mutation.success ).validate( "foo" ) shouldBe
            invalidNel( "condition" )
        ( condition.failure && mutation.failure ).validate( "foo" ) shouldBe
            invalidNel( "condition" )
    }

    it should "combine Transformations with Conditions" in {
        ( transformation && condition.success ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( transformation && condition.failure ).validate( "foo" ) shouldBe
            invalidNel( "condition" )
        ( condition.success && transformation ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( condition.failure && transformation ).validate( "foo" ) shouldBe
            invalidNel( "condition" )
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

    it should "combine symmetric Mutations" in {
        ( mutation.success & mutation.success ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( mutation.success & mutation.failure ).validate( "foo" ) shouldBe
            invalidNel( "mutation" )
        ( mutation.failure & mutation.success ).validate( "foo" ) shouldBe
            invalidNel( "mutation" )
        ( mutation.failure & mutation.failure ).validate( "foo" ) shouldBe
            invalid ( NonEmptyList.of( "mutation", "mutation" ) )
    }

    it should "not combine asymmetric Mutations" in {
        object mutation2
            extends Mutation.With[String, Int]( _ ⇒ Some( 0 ) )
            with Arguments.None

        assertTypeError( "mutation.success & mutation2" )
        assertTypeError( "mutation2 & mutation.success" )
        assertTypeError( "mutation.failure & mutation2" )
        assertTypeError( "mutation2 & mutation.failure" )
    }

    it should "combine symmetric Transformations" in {
        ( transformation & transformation ).validate( "foo" ) shouldBe
            valid( "foo" )
    }

    it should "not combine asymmetric Transformations" in {
        object transformation2 extends Transformation.With[String, Int]( _ ⇒ 0 )

        assertTypeError( "transformation & transformation2" )
        assertTypeError( "transformation2 & transformation" )
    }

    it should "combine symmetric Mutations with Conditions" in {
        ( mutation.success & condition.success ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( mutation.failure & condition.success ).validate( "foo" ) shouldBe
            invalidNel( "mutation" )
        ( mutation.success & condition.failure ).validate( "foo" ) shouldBe
            invalidNel( "condition" )
        ( mutation.failure & condition.failure ).validate( "foo" ) shouldBe
            invalid ( NonEmptyList.of( "mutation", "condition" ) )
        ( condition.success & mutation.success ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( condition.success & mutation.failure ).validate( "foo" ) shouldBe
            invalidNel( "mutation" )
        ( condition.failure & mutation.success ).validate( "foo" ) shouldBe
            invalidNel( "condition" )
        ( condition.failure & mutation.failure ).validate( "foo" ) shouldBe
            invalid ( NonEmptyList.of( "condition", "mutation" ) )
    }

    it should "not combine asymmetric Mutations with Conditions" in {
        object mutation2
            extends Mutation.With[String, Int]( _ ⇒ Some( 0 ) )
            with Arguments.None

        assertTypeError( "condition.success & mutation2" )
        assertTypeError( "mutation2 & condition.success" )
        assertTypeError( "condition.failure & mutation2" )
        assertTypeError( "mutation2 & condition.failure" )
    }

    it should "combine symmetric Transformations with Conditions" in {
        ( transformation & condition.success ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( transformation & condition.failure ).validate( "foo" ) shouldBe
            invalidNel( "condition" )
        ( condition.success & transformation ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( condition.failure & transformation ).validate( "foo" ) shouldBe
            invalidNel( "condition" )
    }

    it should "not combine asymmetric Transformations with Conditions" in {
        object transformation2 extends Transformation.With[String, Int]( _ ⇒ 0 )

        assertTypeError( "condition.success & transformation2" )
        assertTypeError( "condition.failure & transformation2" )
        assertTypeError( "transformation2 & condition.success" )
        assertTypeError( "transformation2 & condition.failure" )
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

    it should "combine symmetric Mutations" in {
        ( mutation.success || mutation.success ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( mutation.success || mutation.failure ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( mutation.failure || mutation.success ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( mutation.failure || mutation.failure ).validate( "foo" ) shouldBe
            invalid ( NonEmptyList.of( "mutation", "mutation" ) )
    }

    it should "not combine asymmetric Mutations" in {
        object mutation2
            extends Mutation.With[String, Int]( _ ⇒ Some( 0 ) )
            with Arguments.None

        assertTypeError( "mutation.success || mutation2" )
        assertTypeError( "mutation2 || mutation.success" )
        assertTypeError( "mutation.failure || mutation2" )
        assertTypeError( "mutation2 || mutation.failure" )
    }

    it should "combine symmetric Transformations" in {
        ( transformation || transformation ).validate( "foo" ) shouldBe
            valid( "foo" )
    }

    it should "not combine asymmetric Transformations" in {
        object transformation2 extends Transformation.With[String, Int]( _ ⇒ 0 )

        assertTypeError( "transformation || transformation2" )
        assertTypeError( "transformation2 || transformation" )
    }

    it should "combine symmetric Mutations with Conditions" in {
        ( mutation.success || condition.success ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( mutation.failure || condition.success ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( mutation.success || condition.failure ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( mutation.failure || condition.failure ).validate( "foo" ) shouldBe
            invalid ( NonEmptyList.of( "mutation", "condition" ) )
        ( condition.success || mutation.success ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( condition.success || mutation.failure ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( condition.failure || mutation.success ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( condition.failure || mutation.failure ).validate( "foo" ) shouldBe
            invalid ( NonEmptyList.of( "condition", "mutation" ) )
    }

    it should "not combine asymmetric Mutations with Conditions" in {
        object mutation2
            extends Mutation.With[String, Int]( _ ⇒ Some( 0 ) )
            with Arguments.None

        assertTypeError( "condition.success || mutation2" )
        assertTypeError( "mutation2 || condition.success" )
        assertTypeError( "condition.failure || mutation2" )
        assertTypeError( "mutation2 || condition.failure" )
    }

    it should "combine symmetric Transformations with Conditions" in {
        ( transformation || condition.success ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( transformation || condition.failure ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( condition.success || transformation ).validate( "foo" ) shouldBe
            valid( "foo" )
        ( condition.failure || transformation ).validate( "foo" ) shouldBe
            valid( "foo" )
    }

    it should "not combine asymmetric Transformations with Conditions" in {
        object transformation2 extends Transformation.With[String, Int]( _ ⇒ 0 )

        assertTypeError( "condition.success || transformation2" )
        assertTypeError( "condition.failure || transformation2" )
        assertTypeError( "transformation2 || condition.success" )
        assertTypeError( "transformation2 || condition.failure" )
    }
}
