package io.taig.gandalf.core.test

import io.taig.gandalf.core._
import io.taig.gandalf.core.Validation._
import io.taig.gandalf.core.syntax.validation._

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

    it should "combine Transformations" in {
        "foo".confirm( transformation && transformation ) shouldBe
            Some( "foo" )
    }

    it should "not combine Mutations with Conditions" in {
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

    it should "combine Transformations with Conditions" in {
        "foo".confirm( transformation && condition.success ) shouldBe
            Some( "foo" )
        "foo".confirm( transformation && condition.failure ) shouldBe
            None
        "foo".confirm( condition.success && transformation ) shouldBe
            Some( "foo" )
        "foo".confirm( condition.failure && transformation ) shouldBe
            None
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

    it should "combine symmetric Mutations" in {
        "foo".confirm( mutation.success || mutation.success ) shouldBe
            Some( "foo" )
        "foo".confirm( mutation.success || mutation.failure ) shouldBe
            Some( "foo" )
        "foo".confirm( mutation.failure || mutation.success ) shouldBe
            Some( "foo" )
        "foo".confirm( mutation.failure || mutation.failure ) shouldBe
            None
    }

    it should "not combine asymmetric Mutations" in {
        trait mutation2

        object mutation2 extends mutation2 {
            implicit val stringInt: Mutation[mutation2, String, Int] = ???
        }

        assertTypeError( """"foo".confirm( "mutation.success || mutation2" )""" )
        assertTypeError( """"foo".confirm( "mutation2 || mutation.success" )""" )
        assertTypeError( """"foo".confirm( "mutation.failure || mutation2" )""" )
        assertTypeError( """"foo".confirm( "mutation2 || mutation.failure" )""" )
    }

    it should "not combine Transformations" in {
        assertTypeError( """"foo".confirm( transformation || transformation )""" )
    }

    it should "combine symmetric Mutations with Conditions" in {
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

    it should "not combine asymmetric Mutations with Conditions" in {
        trait mutation2

        object mutation2 extends mutation2 {
            implicit val stringInt: Mutation[mutation2, String, Int] = ???
        }

        assertTypeError( """"foo".confirm( "condition.success || mutation2" )""" )
        assertTypeError( """"foo".confirm( "mutation2 || condition.success" )""" )
        assertTypeError( """"foo".confirm( "condition.failure || mutation2" )""" )
        assertTypeError( """"foo".confirm( "mutation2 || condition.failure" )""" )
    }

    it should "not combine Transformations with Conditions" in {
        assertTypeError( """"foo".confirm( transformation || conditions.success )""" )
        assertTypeError( """"foo".confirm( conditions.success || transformation )""" )
    }

    it should "provide a String representation" in {
        ( condition.success || condition.failure ).serialize shouldBe
            "(success || failure)"
    }
}