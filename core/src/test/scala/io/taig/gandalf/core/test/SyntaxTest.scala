package io.taig.gandalf.core.test

import io.taig.gandalf.core.{ Rule, Validation }
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
    //
    //    it should "combine symmetric Mutations" in {
    //        "foo".confirm( mutation.success || mutation.success ) shouldBe
    //            Some( "foo" )
    //        "foo".confirm( mutation.success || mutation.failure ) shouldBe
    //            Some( "foo" )
    //        "foo".confirm( mutation.failure || mutation.success ) shouldBe
    //            Some( "foo" )
    //        "foo".confirm( mutation.failure || mutation.failure ) shouldBe
    //            None
    //    }

    it should "not combine asymmetric Mutations" in {
        trait mutation2 extends Rule.Mutation

        object mutation2 extends mutation2 {
            implicit val stringInt: Validation[mutation2, String, Int] = ???
        }

        //        mutation.success || mutation2

        assertTypeError( "mutation.success || mutation2" )
        assertTypeError( "mutation2 || mutation.success" )
        assertTypeError( "mutation.failure || mutation2" )
        assertTypeError( "mutation2 || mutation.failure" )
    }

    //    it should "not combine Transformations" in {
    //        assertTypeError( """"foo".confirm( transformation || transformation )""" )
    //    }
    //
    //    it should "combine symmetric Mutations with Conditions" in {
    //        "foo".confirm( mutation.success || condition.success ) shouldBe
    //            Some( "foo" )
    //        "foo".confirm( mutation.failure || condition.success ) shouldBe
    //            Some( "foo" )
    //        "foo".confirm( mutation.success || condition.failure ) shouldBe
    //            Some( "foo" )
    //        "foo".confirm( mutation.failure || condition.failure ) shouldBe
    //            None
    //        "foo".confirm( condition.success || mutation.success ) shouldBe
    //            Some( "foo" )
    //        "foo".confirm( condition.success || mutation.failure ) shouldBe
    //            Some( "foo" )
    //        "foo".confirm( condition.failure || mutation.success ) shouldBe
    //            Some( "foo" )
    //        "foo".confirm( condition.failure || mutation.failure ) shouldBe
    //            None
    //    }
    //
    //    it should "not combine asymmetric Mutations with Conditions" in {
    //        trait mutation2
    //
    //        object mutation2 extends mutation2 {
    //            implicit val stringInt: Mutation[mutation2, String, Int] = ???
    //        }
    //
    //        assertTypeError( """"foo".confirm( "condition.success || mutation2" )""" )
    //        assertTypeError( """"foo".confirm( "mutation2 || condition.success" )""" )
    //        assertTypeError( """"foo".confirm( "condition.failure || mutation2" )""" )
    //        assertTypeError( """"foo".confirm( "mutation2 || condition.failure" )""" )
    //    }
    //
    //    it should "not combine Transformations with Conditions" in {
    //        assertTypeError( """"foo".confirm( transformation || conditions.success )""" )
    //        assertTypeError( """"foo".confirm( conditions.success || transformation )""" )
    //    }
    //
    //    it should "provide a String representation" in {
    //        ( condition.success || condition.failure ).serialize shouldBe
    //            "(success || failure)"
    //    }
}