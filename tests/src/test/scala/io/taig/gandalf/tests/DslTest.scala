package io.taig.gandalf.tests

import io.taig.gandalf.Policy
import io.taig.gandalf.ops.dsl.Operator
import io.taig.gandalf.syntax.dsl._
import shapeless.test.illTyped
import shapeless._

class DslTest extends Suite {
    it should "allow to combine equally typed rules" in {
        rule.required && rule.required shouldBe
            Policy( ( rule.required :: HNil ) :: Operator.&& :: ( rule.required :: HNil ) :: HNil )
    }

    it should "permit to combine differently typed rules" in {
        illTyped( "rule.required && rule.gt( 3 )" )
    }

    it should "allow to combine a policy with an equally typed rule" in {
        ( rule.required && rule.required ) && rule.required shouldBe Policy(
            ( ( rule.required :: HNil ) :: Operator.&& :: ( rule.required :: HNil ) :: HNil ) ::
                Operator.&& ::
                ( rule.required :: HNil ) ::
                HNil
        )
    }

    it should "permit to combine a policy with a differently typed rule" in {
        illTyped( "( rule.required && rule.required ) && rule.gt( 3 )" )
    }

    it should "allow to combine a rule with an equally typed policy" in {
        rule.required && ( rule.required && rule.required ) shouldBe Policy(
            ( rule.required :: HNil ) ::
                Operator.&& ::
                ( ( rule.required :: HNil ) :: Operator.&& :: ( rule.required :: HNil ) :: HNil ) ::
                HNil
        )
    }

    it should "permit to combine a rule with a differently typed policy" in {
        illTyped( "rule.gt( 3 ) && ( rule.required && rule.required )" )
    }

    it should "allow to combine equally typed policies" in {
        ( rule.required && rule.required ) && ( rule.required && rule.required ) shouldBe Policy(
            ( ( rule.required :: HNil ) :: Operator.&& :: ( rule.required :: HNil ) :: HNil ) ::
                Operator.&& ::
                ( ( rule.required :: HNil ) :: Operator.&& :: ( rule.required :: HNil ) :: HNil ) ::
                HNil
        )
    }

    it should "permit to combine differently typed policies" in {
        illTyped( "( rule.required && rule.required ) && ( rule.gt( 3 ) && rule.gt( 6 ) )" )
    }

    it should "allow to combine compatibly typed transformations" in {
        transformation.trim ~> transformation.trim shouldBe Policy(
            ( transformation.trim :: HNil ) :: Operator.~> :: ( transformation.trim :: HNil ) :: HNil
        )
    }

    it should "permit to combine incompatibly typed rule transformations" in {
        illTyped( "transformation.parse ~> transformation.trim" )
    }

    it should "allow to combine a policy with a compatibly typed transformation" in {
        ( transformation.trim ~> transformation.trim ) ~> transformation.trim shouldBe Policy(
            ( ( transformation.trim :: HNil ) :: Operator.~> :: ( transformation.trim :: HNil ) :: HNil ) ::
                Operator.~> ::
                ( transformation.trim :: HNil ) ::
                HNil
        )
    }

    it should "permit to combine a policy with an incompatibly typed transformation" in {
        illTyped( "( transformation.trim ~> transformation.parse ) ~> transformation.parse" )
    }

    it should "allow to combine a transformation with a compatibly typed policy" in {
        transformation.trim ~> ( transformation.trim ~> transformation.trim ) shouldBe Policy(
            ( transformation.trim :: HNil ) ::
                Operator.~> ::
                ( ( transformation.trim :: HNil ) :: Operator.~> :: ( transformation.trim :: HNil ) :: HNil ) ::
                HNil
        )
    }

    it should "permit to combine a transformation with an incompatibly typed policy" in {
        illTyped( "transformation.parse ~> ( transformation.trim ~> transformation.trim )" )
    }

    it should "allow to combine compatibly typed policies" in {
        ( transformation.trim ~> transformation.trim ) ~> ( transformation.trim ~> transformation.trim ) shouldBe
            Policy(
                ( ( transformation.trim :: HNil ) :: Operator.~> :: ( transformation.trim :: HNil ) :: HNil ) ::
                    Operator.~> ::
                    ( ( transformation.trim :: HNil ) :: Operator.~> :: ( transformation.trim :: HNil ) :: HNil ) ::
                    HNil
            )
    }

    it should "permit to combine incompatibly typed policies" in {
        illTyped( "( transformation.trim ~> transformation.parse ) ~> ( transformation.trim ~> transformation.trim )" )
    }

    ignore should "permit combining rules with a transformation operator" in {
        // illTyped( "rule.required ~> rule.required" )
    }

    ignore should "permit combining transformations with a rule operator" in {
        // illTyped( "transformation.trim && transformation.trim" )
    }
}