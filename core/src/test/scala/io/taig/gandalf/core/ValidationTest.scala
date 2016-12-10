package io.taig.gandalf.core

import io.taig.gandalf.core._
import io.taig.gandalf.core.syntax.all._
import shapeless.test.illTyped

class ValidationTest extends Suite {
    "Condition" should "validate" in {
        Validation[condition.success, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[condition.failure, String, String].confirm( "foo" ) shouldBe
            None
    }

    "Mutation" should "validate" in {
        Validation[mutation.success, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[mutation.failure, String, String].confirm( "foo" ) shouldBe
            None
    }

    "Transition" should "validate" in {
        Validation[transition.string, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
    }

    "&&" should "validate Conditions" in {
        Validation[condition.success && condition.success, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[condition.success && condition.failure, String, String].confirm( "foo" ) shouldBe
            None
        Validation[condition.failure && condition.success, String, String].confirm( "foo" ) shouldBe
            None
        Validation[condition.failure && condition.failure, String, String].confirm( "foo" ) shouldBe
            None
    }

    it should "not validate incompatibly typed Conditions" in {
        illTyped( "Validation[condition.success && condition.int, String, Int]" )
        illTyped( "Validation[condition.int && condition.success, Int, String]" )
    }

    it should "validate Mutations" in {
        Validation[mutation.success && mutation.success, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[mutation.success && mutation.failure, String, String].confirm( "foo" ) shouldBe
            None
        Validation[mutation.failure && mutation.success, String, String].confirm( "foo" ) shouldBe
            None
        Validation[mutation.failure && mutation.failure, String, String].confirm( "foo" ) shouldBe
            None
    }

    it should "not validate incompatibly typed Mutations" in {
        illTyped( "Validation[mutation.success && mutation.int, String, Int]" )
        illTyped( "Validation[mutation.int && mutation.success, Int, String]" )
    }

    it should "validate asymmetrically typed Mutations" in {
        Validation[mutation.stringInt && mutation.intString, String, String].confirm( "42" ) shouldBe
            Some( "42" )
        Validation[mutation.intString && mutation.stringInt, Int, Int].confirm( 42 ) shouldBe
            Some( 42 )
    }

    it should "validate Transitions" in {
        Validation[transition.string && transition.string, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
    }

    it should "not validate incompatibly typed Transitions" in {
        illTyped( "Validation[transition.success && transition.int, String, Int]" )
        illTyped( "Validation[transition.int && transition.success, Int, String]" )
    }

    it should "validate asymmetrically typed Transitions" in {
        Validation[transition.stringInt && transition.intString, String, String].confirm( "42" ) shouldBe
            Some( "42" )
        Validation[transition.intString && transition.stringInt, Int, Int].confirm( 42 ) shouldBe
            Some( 42 )
    }

    it should "validate Mutations with Conditions" in {
        Validation[mutation.success && condition.success, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[mutation.failure && condition.success, String, String].confirm( "foo" ) shouldBe
            None
        Validation[mutation.success && condition.failure, String, String].confirm( "foo" ) shouldBe
            None
        Validation[mutation.failure && condition.failure, String, String].confirm( "foo" ) shouldBe
            None
        Validation[condition.success && mutation.success, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[condition.success && mutation.failure, String, String].confirm( "foo" ) shouldBe
            None
        Validation[condition.failure && mutation.success, String, String].confirm( "foo" ) shouldBe
            None
        Validation[condition.failure && mutation.failure, String, String].confirm( "foo" ) shouldBe
            None
    }

    it should "not validate incompatibly typed Mutations with Conditions" in {
        illTyped( "Validation[condition.success && mutation.int, String, Int]" )
        illTyped( "Validation[mutation.int && condition.success, Int, String]" )
    }

    it should "validate asymmetrically typed Mutations with Conditions" in {
        Validation[mutation.stringInt && condition.int, String, Int].confirm( "42" ) shouldBe
            Some( 42 )
        Validation[condition.int && mutation.intString, Int, String].confirm( 42 ) shouldBe
            Some( "42" )
    }

    it should "validate Transitions with Conditions" in {
        Validation[transition.string && condition.success, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[transition.string && condition.failure, String, String].confirm( "foo" ) shouldBe
            None
        Validation[condition.success && transition.string, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[condition.failure && transition.string, String, String].confirm( "foo" ) shouldBe
            None
    }

    it should "not validate incompatibly typed Transitions with Conditions" in {
        illTyped( "Validation[condition.success && transition.int, String, Int]" )
        illTyped( "Validation[transition.int && condition.success, Int, String]" )
    }

    it should "validate asymmetrically typed Transitions with Conditions" in {
        Validation[transition.stringInt && condition.int, String, Int].confirm( "42" ) shouldBe
            Some( 42 )
        42.confirm( condition.int && transition.intString ) shouldBe
            Some( "42" )
    }

    it should "validate compositions" in {
        Validation[composition.and.success, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[composition.and.failure, String, String].confirm( "foo" ) shouldBe
            None
    }

    it should "validate compositions with not elements" in {
        Validation[composition.and.notL, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[composition.and.notR, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
    }

    it should "validate combinations with compositions" in {
        Validation[composition.and.notL && condition.success, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[composition.and.notR && condition.success, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
    }

    "||" should "validate Conditions" in {
        Validation[condition.success || condition.success, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[condition.success || condition.failure, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[condition.failure || condition.success, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[condition.failure || condition.failure, String, String].confirm( "foo" ) shouldBe
            None
    }

    it should "not validate incompatibly typed Conditions" in {
        illTyped( "Validation[condition.success || condition.int, String, Int]" )
        illTyped( "Validation[condition.int || condition.success, Int, String]" )
    }

    it should "validate Mutations" in {
        Validation[mutation.success || mutation.success, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[mutation.success || mutation.failure, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[mutation.failure || mutation.success, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[mutation.failure || mutation.failure, String, String].confirm( "foo" ) shouldBe
            None
    }

    it should "not validate incompatibly typed Mutations" in {
        illTyped( "Validation[mutation.success || mutation.int, String, Int]" )
        illTyped( "Validation[mutation.int || mutation.success, Int, String]" )
    }

    it should "not validate asymmetrically typed Mutations" in {
        illTyped( "Validation[mutation.stringInt || mutation.string, String, Int]" )
        illTyped( "Validation[mutation.string || mutation.stringInt, Int, String]" )
    }

    it should "not validate Transitions" in {
        illTyped( "Validation[transition.string || transition.string, String, String]" )
    }

    it should "not validate incompatibly typed Transitions" in {
        illTyped( "Validation[transition.string || transition.int, String, Int]" )
        illTyped( "Validation[transition.int || transition.string, Int, String]" )
    }

    it should "not validate asymmetrically typed Transitions" in {
        illTyped( "Validation[transition.stringInt || transition.intString, String, Int]" )
        illTyped( "Validation[transition.intString || transition.stringInt, Int, String]" )
    }

    it should "validate Mutations with Conditions" in {
        Validation[mutation.success || condition.success, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[mutation.failure || condition.success, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[mutation.success || condition.failure, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[mutation.failure || condition.failure, String, String].confirm( "foo" ) shouldBe
            None
        Validation[condition.success || mutation.success, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[condition.success || mutation.failure, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[condition.failure || mutation.success, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[condition.failure || mutation.failure, String, String].confirm( "foo" ) shouldBe
            None
    }

    it should "not validate incompatibly typed Mutations with Conditions" in {
        illTyped( "Validation[condition.success || mutation.int, String, Int]" )
        illTyped( "Validation[mutation.int || condition.success, Int, String]" )
    }

    it should "not validate asymmetrically typed Mutations with Conditions" in {
        illTyped( "Validation[mutation.stringInt || condition.int, String, Int]" )
        illTyped( "Validation[condition.int || mutation.stringInt, Int, String]" )
    }

    it should "not validate Transitions with Conditions" in {
        illTyped( "Validation[transition.string || condition.success, String, String]" )
        illTyped( "Validation[condition.success || transition.string, String, String]" )
    }

    it should "not validate incompatibly typed Transitions with Conditions" in {
        illTyped( "Validation[condition.success || transition.int, String, Int]" )
        illTyped( "Validation[transition.int || condition.success, Int, String]" )
    }

    it should "not validate asymmetrically typed Transitions with Conditions" in {
        illTyped( "Validation[transition.intString || condition.success, Int, String]" )
        illTyped( "Validation[condition.success || transition.stringInt, String, Int]" )
    }

    it should "validate compositions" in {
        Validation[composition.or.success, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[composition.or.failure, String, String].confirm( "foo" ) shouldBe
            None
    }

    it should "validate compositions with not elements" in {
        Validation[composition.or.notL, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[composition.or.notR, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
    }

    it should "validate combinations with compositions" in {
        Validation[composition.and.notL || condition.success, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[composition.and.notR || condition.success, String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
    }

    "not" should "validate Condition" in {
        Validation[not[condition.failure], String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[not[condition.success], String, String].confirm( "foo" ) shouldBe
            None
    }

    it should "not validate Mutation" in {
        illTyped( "Validation[not[mutation.failure], String, String]" )
        illTyped( "Validation[not[mutation.success], String, String]" )
    }

    it should "not validate Transition" in {
        illTyped( "Validation[not[transition.string], String, String]" )
    }

    "not[&&]" should "validate Conditions" in {
        Validation[not[condition.success && condition.failure], String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[not[condition.success && condition.success], String, String].confirm( "foo" ) shouldBe
            None
    }

    it should "validate compositions" in {
        Validation[not[composition.and.failure], String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[not[composition.and.success], String, String].confirm( "foo" ) shouldBe
            None
    }

    it should "validate compositions with not elements" in {
        Validation[not[composition.and.notL], String, String].confirm( "foo" ) shouldBe
            None
        Validation[not[composition.and.notR], String, String].confirm( "foo" ) shouldBe
            None
    }

    "not[||]" should "validate Conditions" in {
        Validation[not[condition.failure || condition.failure], String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[not[condition.failure || condition.success], String, String].confirm( "foo" ) shouldBe
            None
    }

    it should "validate compositions" in {
        Validation[not[composition.or.failure], String, String].confirm( "foo" ) shouldBe
            Some( "foo" )
        Validation[not[composition.or.success], String, String].confirm( "foo" ) shouldBe
            None
    }

    it should "validate compositions with not elements" in {
        Validation[not[composition.or.notL], String, String].confirm( "foo" ) shouldBe
            None
        Validation[not[composition.or.notR], String, String].confirm( "foo" ) shouldBe
            None
    }
}