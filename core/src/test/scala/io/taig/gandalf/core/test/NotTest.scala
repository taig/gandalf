package io.taig.gandalf.core.test

import io.taig.gandalf.core.syntax.all._
import io.taig.gandalf.core.{ &&, ||, not ⇒ dont }
import shapeless.test.illTyped

class NotTest extends Suite {
    it should "provide a String representation" in {
        dont( condition.success ).serialize shouldBe "not(success)"
        dont( condition.failure ).serialize shouldBe "not(failure)"
    }

    it should "fail validation when the underlying Condition succeeds" in {
        "foo".confirm( dont( condition.success ) ) shouldBe
            None
    }

    it should "succeed validation when the underlying Condition fails" in {
        "foo".confirm( dont( condition.failure ) ) shouldBe
            Some( "foo" )
    }

    it should "not support Mutations" in {
        illTyped( "dont( mutation.success )", "Invalid operation" )
        illTyped( "dont( mutation.failure )", "Invalid operation" )
    }

    it should "not support Transitions" in {
        illTyped( "dont( transition.string )", "Invalid operation" )
    }

    it should "have no effect when applied twice" in {
        "foo".confirm( dont( dont( condition.success ) ) ) shouldBe
            Some( "foo" )
        "foo".confirm( dont( dont( condition.failure ) ) ) shouldBe
            None
    }

    "&&" should "support negated Conditions" in {
        "foo".confirm( dont( condition.success && condition.failure ) ) shouldBe
            Some( "foo" )
        "foo".confirm( dont( condition.success && condition.success ) ) shouldBe
            None
    }

    it should "support negated operands" in {
        "foo".confirm( condition.success && dont( condition.failure ) ) shouldBe
            Some( "foo" )
        "foo".confirm( dont( condition.success ) && condition.success ) shouldBe
            None
    }

    it should "support nested negations" in {
        "foo".confirm( dont( condition.success && dont( condition.failure ) ) ) shouldBe
            None
        "foo".confirm( dont( condition.success && dont( condition.success ) ) ) shouldBe
            Some( "foo" )
        "foo".confirm( dont( condition.failure && dont( condition.failure ) ) ) shouldBe
            Some( "foo" )
        "foo".confirm( dont( condition.failure && dont( condition.success ) ) ) shouldBe
            Some( "foo" )
    }

    it should "support Mutations with Conditions" in {
        "foo".confirm( dont( condition.success && mutation.success ) ) shouldBe
            None
        "foo".confirm( dont( condition.failure && mutation.success ) ) shouldBe
            Some( "foo" )
        "foo".confirm( dont( mutation.success && condition.success ) ) shouldBe
            None
        "foo".confirm( dont( mutation.success && condition.failure ) ) shouldBe
            Some( "foo" )
        "foo".confirm( dont( condition.success && mutation.failure ) ) shouldBe
            None
        "foo".confirm( dont( condition.failure && mutation.failure ) ) shouldBe
            None
        "foo".confirm( dont( mutation.failure && condition.success ) ) shouldBe
            None
        "foo".confirm( dont( mutation.failure && condition.failure ) ) shouldBe
            None
    }

    it should "support Transitions with Conditions" in {
        "foo".confirm( dont( condition.success && transition.string ) ) shouldBe
            None
        "foo".confirm( dont( condition.failure && transition.string ) ) shouldBe
            Some( "foo" )
        "foo".confirm( dont( transition.string && condition.success ) ) shouldBe
            None
        "foo".confirm( dont( transition.string && condition.failure ) ) shouldBe
            Some( "foo" )
    }

    "||" should "support negated Conditions" in {
        "foo".confirm( dont( condition.success || condition.failure ) ) shouldBe
            None
        "foo".confirm( dont( condition.failure || condition.failure ) ) shouldBe
            Some( "foo" )
    }

    it should "support negated operands" in {
        "foo".confirm( condition.failure || dont( condition.failure ) ) shouldBe
            Some( "foo" )
        "foo".confirm( dont( condition.success ) || condition.failure ) shouldBe
            None
    }

    it should "support nested negations" in {
        "foo".confirm( dont( condition.success || dont( condition.failure ) ) ) shouldBe
            None
        "foo".confirm( dont( condition.success || dont( condition.success ) ) ) shouldBe
            None
        "foo".confirm( dont( condition.failure || dont( condition.failure ) ) ) shouldBe
            None
        "foo".confirm( dont( condition.failure || dont( condition.success ) ) ) shouldBe
            Some( "foo" )
    }

    it should "support negated custom Conditions" in {
        object success extends ( condition.success || condition.success )
        "foo".confirm( dont( success ) ) shouldBe
            None

        object failure extends ( condition.failure || condition.failure )
        "foo".confirm( dont( failure ) ) shouldBe Some( "foo" )
    }

    it should "support Mutations with Conditions" in {
        "foo".confirm( dont( condition.success || mutation.success ) ) shouldBe
            Some( "foo" )
        "foo".confirm( dont( condition.failure || mutation.success ) ) shouldBe
            Some( "foo" )
        "foo".confirm( dont( mutation.success || condition.success ) ) shouldBe
            Some( "foo" )
        "foo".confirm( dont( mutation.success || condition.failure ) ) shouldBe
            Some( "foo" )
        "foo".confirm( dont( condition.success || mutation.failure ) ) shouldBe
            None
        "foo".confirm( dont( condition.failure || mutation.failure ) ) shouldBe
            Some( "foo" )
        "foo".confirm( dont( mutation.failure || condition.success ) ) shouldBe
            None
        "foo".confirm( dont( mutation.failure || condition.failure ) ) shouldBe
            Some( "foo" )
    }

    it should "not support Transitions with Conditions" in {
        illTyped( "dont( condition.success || transition.string )", "Invalid operation" )
        illTyped( "dont( transition.string || condition.success )", "Invalid operation" )
    }
}