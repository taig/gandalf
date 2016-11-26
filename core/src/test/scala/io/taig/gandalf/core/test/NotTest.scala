package io.taig.gandalf.core.test

import io.taig.gandalf.core.syntax.all._
import io.taig.gandalf.core.{ not ⇒ dont }

class NotTest extends Suite {
    it should "fail validation when the underlying Condition succeeds" in {
        "foo".confirm( dont( condition.success ) ) shouldBe
            None
    }

    it should "succeed validation when the underlying Condition fails" in {
        "foo".confirm( dont( condition.failure ) ) shouldBe
            Some( "foo" )
    }

    //    it should "not support Mutations" in {
    //        assertTypeError( """"foo".confirm( dont( mutation.success ) )""" )
    //        assertTypeError( """"foo".confirm( dont( mutation.failure ) )""" )
    //    }
    //
    //    it should "not support Transformations" in {
    //        assertTypeError( """"foo".confirm( dont( transformation ) )""" )
    //    }
    //
    //    it should "have no effect when applied twice" in {
    //        "foo".confirm( dont( dont( condition.success ) ) ) shouldBe
    //            Some( "foo" )
    //        "foo".confirm( dont( dont( condition.failure ) ) ) shouldBe
    //            None
    //    }
    //
    //    "&&" should "support Conditions" in {
    //        "foo".confirm( dont( condition.success && condition.failure ) ) shouldBe
    //            Some( "foo" )
    //        "foo".confirm( dont( condition.success && condition.success ) ) shouldBe
    //            None
    //    }
    //
    //    it should "support custom Conditions" in {
    //        object success extends ( condition.success && condition.success )
    //        "foo".confirm( dont( success ) ) shouldBe
    //            None
    //
    //        object failure extends ( condition.success && condition.failure )
    //        "foo".confirm( dont( failure ) ) shouldBe Some( "foo" )
    //    }
    //
    //    it should "support Mutations with Conditions" in {
    //        "foo".confirm( dont( condition.success && mutation.success ) ) shouldBe
    //            None
    //        "foo".confirm( dont( condition.failure && mutation.success ) ) shouldBe
    //            Some( "foo" )
    //        "foo".confirm( dont( mutation.success && condition.success ) ) shouldBe
    //            None
    //        "foo".confirm( dont( mutation.success && condition.failure ) ) shouldBe
    //            Some( "foo" )
    //        "foo".confirm( dont( condition.success && mutation.failure ) ) shouldBe
    //            None
    //        "foo".confirm( dont( condition.failure && mutation.failure ) ) shouldBe
    //            None
    //        "foo".confirm( dont( mutation.failure && condition.success ) ) shouldBe
    //            None
    //        "foo".confirm( dont( mutation.failure && condition.failure ) ) shouldBe
    //            None
    //    }
    //
    //    it should "support Transformations with Conditions" in {
    //        "foo".confirm( dont( condition.success && transformation ) ) shouldBe
    //            None
    //        "foo".confirm( dont( condition.failure && transformation ) ) shouldBe
    //            Some( "foo" )
    //        "foo".confirm( dont( transformation && condition.success ) ) shouldBe
    //            None
    //        "foo".confirm( dont( transformation && condition.failure ) ) shouldBe
    //            Some( "foo" )
    //    }
    //
    //    it should "support nesting" in {
    //        "foo".confirm( dont( condition.success && dont( condition.failure ) ) ) shouldBe
    //            None
    //        "foo".confirm( dont( condition.success && dont( condition.success ) ) ) shouldBe
    //            Some( "foo" )
    //        "foo".confirm( dont( condition.failure && dont( condition.failure ) ) ) shouldBe
    //            Some( "foo" )
    //        "foo".confirm( dont( condition.failure && dont( condition.success ) ) ) shouldBe
    //            Some( "foo" )
    //    }
    //
    //    "||" should "support Conditions" in {
    //        "foo".confirm( dont( condition.success || condition.failure ) ) shouldBe
    //            None
    //        "foo".confirm( dont( condition.failure || condition.failure ) ) shouldBe
    //            Some( "foo" )
    //    }
    //
    //    it should "support custom Conditions" in {
    //        object success extends ( condition.success.type || condition.success.type )
    //        "foo".confirm( dont( success ) ) shouldBe
    //            None
    //
    //        object failure extends ( condition.failure.type || condition.failure.type )
    //        "foo".confirm( dont( failure ) ) shouldBe Some( "foo" )
    //    }
    //
    //    it should "support Mutations with Conditions" in {
    //        "foo".confirm( dont( condition.success || mutation.success ) ) shouldBe
    //            Some( "foo" )
    //        "foo".confirm( dont( condition.failure || mutation.success ) ) shouldBe
    //            Some( "foo" )
    //        "foo".confirm( dont( mutation.success || condition.success ) ) shouldBe
    //            Some( "foo" )
    //        "foo".confirm( dont( mutation.success || condition.failure ) ) shouldBe
    //            Some( "foo" )
    //        "foo".confirm( dont( condition.success || mutation.failure ) ) shouldBe
    //            None
    //        "foo".confirm( dont( condition.failure || mutation.failure ) ) shouldBe
    //            Some( "foo" )
    //        "foo".confirm( dont( mutation.failure || condition.success ) ) shouldBe
    //            None
    //        "foo".confirm( dont( mutation.failure || condition.failure ) ) shouldBe
    //            Some( "foo" )
    //    }
    //
    //    it should "not support Transformations with Conditions" in {
    //        assertTypeError( """"foo".confirm( dont( condition.success || transformation ) )""" )
    //        assertTypeError( """"foo".confirm( dont( transformation || condition.success ) )""" )
    //    }
    //
    //    it should "support nesting" in {
    //        "foo".confirm( dont( condition.success || dont( condition.failure ) ) ) shouldBe
    //            None
    //        "foo".confirm( dont( condition.success || dont( condition.success ) ) ) shouldBe
    //            None
    //        "foo".confirm( dont( condition.failure || dont( condition.failure ) ) ) shouldBe
    //            None
    //        "foo".confirm( dont( condition.failure || dont( condition.success ) ) ) shouldBe
    //            Some( "foo" )
    //    }
}