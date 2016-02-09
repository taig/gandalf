package io.taig.bsts

import io.taig.bsts.ops.dsl.Operator
import io.taig.bsts.syntax.dsl._
import shapeless.test.illTyped
import shapeless._

class DslTest extends Suite {
    it should "allow to combine equally typed rules" in {
        rule.required && rule.required shouldBe Policy.Rule(
            ( rule.required :: HNil ) :: Operator.&& :: ( rule.required :: HNil ) :: HNil
        )
    }

    it should "permit to combine differently typed rules" in {
        illTyped( "rule.required && rule.gt( 3 )" )
    }

    it should "allow to combine a rule policy with an equally typed rule" in {
        ( rule.required && rule.required ) && rule.required shouldBe Policy.Rule(
            ( ( rule.required :: HNil ) :: Operator.&& :: ( rule.required :: HNil ) :: HNil ) ::
                Operator.&& ::
                ( rule.required :: HNil ) ::
                HNil
        )
    }

    it should "permit to combine a rule policy with a differently typed rule" in {
        illTyped( "( rule.required && rule.required ) && rule.gt( 3 )" )
    }

    it should "allow to combine a rule with an equally typed rule policy" in {
        rule.required && ( rule.required && rule.required ) shouldBe Policy.Rule(
            ( rule.required :: HNil ) ::
                Operator.&& ::
                ( ( rule.required :: HNil ) :: Operator.&& :: ( rule.required :: HNil ) :: HNil ) ::
                HNil
        )
    }

    it should "permit to combine a rule with a differently typed rule policy" in {
        illTyped( "rule.gt( 3 ) && ( rule.required && rule.required )" )
    }

    it should "allow to combine equally typed rule policies" in {
        ( rule.required && rule.required ) && ( rule.required && rule.required ) shouldBe Policy.Rule(
            ( ( rule.required :: HNil ) :: Operator.&& :: ( rule.required :: HNil ) :: HNil ) ::
                Operator.&& ::
                ( ( rule.required :: HNil ) :: Operator.&& :: ( rule.required :: HNil ) :: HNil ) ::
                HNil
        )
    }

    it should "permit to combine differently typed rule policies" in {
        illTyped( "( rule.required && rule.required ) && ( rule.gt( 3 ) && rule.gt( 6 ) )" )
    }

    it should "allow to combine compatibly typed transformations" in {
        transformation.trim ~> transformation.trim shouldBe Policy.Transformation(
            ( transformation.trim :: HNil ) :: Operator.~> :: ( transformation.trim :: HNil ) :: HNil
        )
    }

    it should "permit to combine incompatibly typed rule policies" in {
        illTyped( "transformation.parse ~> transformation.trim" )
    }

    it should "allow to combine a transformation policy with a compatibly typed transformation" in {
        ( transformation.trim ~> transformation.trim ) ~> transformation.trim shouldBe Policy.Transformation(
            ( ( transformation.trim :: HNil ) :: Operator.~> :: ( transformation.trim :: HNil ) :: HNil ) ::
                Operator.~> ::
                ( transformation.trim :: HNil ) ::
                HNil
        )
    }

    it should "permit to combine a transformation policy with an incompatibly typed transformation" in {
        illTyped( "( transformation.trim ~> transformation.parse ) ~> transformation.parse" )
    }

    it should "allow to combine a transformation with a compatibly typed transformation policy" in {
        transformation.trim ~> ( transformation.trim ~> transformation.trim ) shouldBe Policy.Transformation(
            ( transformation.trim :: HNil ) ::
                Operator.~> ::
                ( ( transformation.trim :: HNil ) :: Operator.~> :: ( transformation.trim :: HNil ) :: HNil ) ::
                HNil
        )
    }

    it should "permit to combine a transformation with an incompatibly typed transformation policy" in {
        illTyped( "transformation.parse ~> ( transformation.trim ~> transformation.trim )" )
    }

    it should "allow to combine compatibly typed transformation policies" in {
        ( transformation.trim ~> transformation.trim ) ~> ( transformation.trim ~> transformation.trim ) shouldBe
            Policy.Transformation(
                ( ( transformation.trim :: HNil ) :: Operator.~> :: ( transformation.trim :: HNil ) :: HNil ) ::
                    Operator.~> ::
                    ( ( transformation.trim :: HNil ) :: Operator.~> :: ( transformation.trim :: HNil ) :: HNil ) ::
                    HNil
            )
    }

    it should "permit to combine incompatibly typed transformation policies" in {
        illTyped( "( transformation.trim ~> transformation.parse ) ~> ( transformation.trim ~> transformation.trim )" )
    }

    it should "permit combining rules with a transformation operator" in {
        illTyped( "rule.required ~> rule.required" )
    }

    it should "permit combining transformations with a rule operator" in {
        illTyped( "transformation.trim && transformation.trim" )
    }
}