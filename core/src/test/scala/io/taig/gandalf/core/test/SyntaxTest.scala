package io.taig.gandalf.core.test

import io.taig.gandalf.core.syntax.all._
import shapeless.test.illTyped

class SyntaxTest extends Suite {
    "&&" should "combine Conditions" in {
        "foo".confirm( condition.success && condition.success ) shouldBe
            Some( "foo" )
        "foo".confirm( condition.success && condition.failure ) shouldBe
            None
        "foo".confirm( condition.failure && condition.success ) shouldBe
            None
        "foo".confirm( condition.failure && condition.failure ) shouldBe
            None
    }

    it should "not combine incompatibly typed Conditions" in {
        illTyped( "condition.success && condition.int", "Invalid operation" )
        illTyped( "condition.int && condition.success", "Invalid operation" )
    }

    it should "combine Mutations" in {
        "foo".confirm( mutation.success && mutation.success ) shouldBe
            Some( "foo" )
        "foo".confirm( mutation.success && mutation.failure ) shouldBe
            None
        "foo".confirm( mutation.failure && mutation.success ) shouldBe
            None
        "foo".confirm( mutation.failure && mutation.failure ) shouldBe
            None
    }

    it should "not combine incompatibly typed Mutations" in {
        illTyped( "mutation.success && mutation.int", "Invalid operation" )
        illTyped( "mutation.int && mutation.success", "Invalid operation" )
    }

    it should "combine asymmetrically typed Mutations" in {
        "42".confirm( mutation.stringInt && mutation.intString ) shouldBe
            Some( "42" )
        42.confirm( mutation.intString && mutation.stringInt ) shouldBe
            Some( 42 )
    }

    it should "combine Transitions" in {
        "foo".confirm( transition.string && transition.string ) shouldBe
            Some( "foo" )
    }

    it should "not combine incompatibly typed Transitions" in {
        illTyped( "transition.string && transition.int", "Invalid operation" )
        illTyped( "transition.int && transition.string", "Invalid operation" )
    }

    it should "combine asymmetrically typed Transitions" in {
        "42".confirm( transition.stringInt && transition.intString ) shouldBe
            Some( "42" )
        42.confirm( transition.intString && transition.stringInt ) shouldBe
            Some( 42 )
    }

    it should "combine Mutations with Conditions" in {
        "foo".confirm( mutation.success && condition.success ) shouldBe
            Some( "foo" )
        "foo".confirm( mutation.failure && condition.success ) shouldBe
            None
        "foo".confirm( mutation.success && condition.failure ) shouldBe
            None
        "foo".confirm( mutation.failure && condition.failure ) shouldBe
            None
        "foo".confirm( condition.success && mutation.success ) shouldBe
            Some( "foo" )
        "foo".confirm( condition.success && mutation.failure ) shouldBe
            None
        "foo".confirm( condition.failure && mutation.success ) shouldBe
            None
        "foo".confirm( condition.failure && mutation.failure ) shouldBe
            None
    }

    it should "not combine incompatibly typed Mutations with Conditions" in {
        illTyped( "mutation.int && condition.success", "Invalid operation" )
        illTyped( "condition.success && mutation.int", "Invalid operation" )
    }

    it should "combine asymmetrically typed Mutations with Conditions" in {
        "42".confirm( mutation.stringInt && condition.int ) shouldBe
            Some( 42 )
        42.confirm( condition.int && mutation.intString ) shouldBe
            Some( "42" )
    }

    it should "combine Transitions with Conditions" in {
        "foo".confirm( transition.string && condition.success ) shouldBe
            Some( "foo" )
        "foo".confirm( transition.string && condition.failure ) shouldBe
            None
        "foo".confirm( condition.success && transition.string ) shouldBe
            Some( "foo" )
        "foo".confirm( condition.failure && transition.string ) shouldBe
            None
    }

    it should "not combine incompatibly typed Transitions with Conditions" in {
        illTyped( "transition.int && condition.success", "Invalid operation" )
        illTyped( "condition.success && transition.int", "Invalid operation" )
    }

    it should "combine asymmetrically typed Transitions with Conditions" in {
        "42".confirm( transition.stringInt && condition.int ) shouldBe
            Some( 42 )
        42.confirm( condition.int && transition.intString ) shouldBe
            Some( "42" )
    }

    it should "provide a String representation" in {
        ( condition.success && condition.failure ).serialize shouldBe
            "(success && failure)"
    }

    "||" should "combine Conditions" in {
        "foo".confirm( condition.success || condition.success ) shouldBe
            Some( "foo" )
        "foo".confirm( condition.success || condition.failure ) shouldBe
            Some( "foo" )
        "foo".confirm( condition.failure || condition.success ) shouldBe
            Some( "foo" )
        "foo".confirm( condition.failure || condition.failure ) shouldBe
            None
    }

    it should "not combine incompatibly typed Conditions" in {
        illTyped( "condition.success || condition.int", "Invalid operation" )
        illTyped( "condition.int || condition.success", "Invalid operation" )
    }

    it should "combine Mutations" in {
        "foo".confirm( mutation.success || mutation.success ) shouldBe
            Some( "foo" )
        "foo".confirm( mutation.success || mutation.failure ) shouldBe
            Some( "foo" )
        "foo".confirm( mutation.failure || mutation.success ) shouldBe
            Some( "foo" )
        "foo".confirm( mutation.failure || mutation.failure ) shouldBe
            None
    }

    it should "not combine incompatibly typed Mutations" in {
        illTyped( "mutation.success || mutation.int", "Invalid operation" )
        illTyped( "mutation.int || mutation.success", "Invalid operation" )
    }

    it should "not combine asymmetrically typed Mutations" in {
        illTyped( "mutation.stringInt || mutation.intString", "Invalid operation" )
        illTyped( "mutation.intString || mutation.stringInt", "Invalid operation" )
    }

    it should "not combine Transitions" in {
        illTyped( "transition.string || transition.string", "Invalid operation" )
    }

    it should "not combine incompatibly typed Transitions" in {
        illTyped( "transition.string || transition.int", "Invalid operation" )
        illTyped( "transition.int || transition.string", "Invalid operation" )
    }

    it should "not combine asymmetrically typed Transitions" in {
        illTyped( "transition.stringInt || transition.intString", "Invalid operation" )
        illTyped( "transition.intString || transition.stringInt", "Invalid operation" )
    }

    it should "combine Mutations with Conditions" in {
        "foo".confirm( mutation.success || condition.success ) shouldBe
            Some( "foo" )
        "foo".confirm( mutation.failure || condition.success ) shouldBe
            Some( "foo" )
        "foo".confirm( mutation.success || condition.failure ) shouldBe
            Some( "foo" )
        "foo".confirm( mutation.failure || condition.failure ) shouldBe
            None
        "foo".confirm( condition.success || mutation.success ) shouldBe
            Some( "foo" )
        "foo".confirm( condition.success || mutation.failure ) shouldBe
            Some( "foo" )
        "foo".confirm( condition.failure || mutation.success ) shouldBe
            Some( "foo" )
        "foo".confirm( condition.failure || mutation.failure ) shouldBe
            None
    }

    it should "not combine incompatibly typed Mutations with Conditions" in {
        illTyped( "mutation.int || condition.success", "Invalid operation" )
        illTyped( "condition.success || mutation.int", "Invalid operation" )
    }

    it should "not combine asymmetrically typed Mutations with Conditions" in {
        illTyped( "mutation.stringInt || condition.int", "Invalid operation" )
        illTyped( "condition.int || mutation.intString", "Invalid operation" )
    }

    it should "not combine Transitions with Conditions" in {
        illTyped( "transition.string || condition.success", "Invalid operation" )
        illTyped( "condition.success || transition.string", "Invalid operation" )
    }

    it should "not combine incompatibly typed Transitions with Conditions" in {
        illTyped( "transition.int || condition.success", "Invalid operation" )
        illTyped( "condition.success || transition.int", "Invalid operation" )
    }

    it should "not combine asymmetrically typed Transitions with Conditions" in {
        illTyped( "transition.intString || condition.success", "Invalid operation" )
        illTyped( "condition.success || transition.stringInt", "Invalid operation" )
    }

    it should "provide a String representation" in {
        ( condition.success || condition.failure ).serialize shouldBe
            "(success || failure)"
    }
}