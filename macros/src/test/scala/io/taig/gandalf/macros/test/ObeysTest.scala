package io.taig.gandalf.macros.test

import io.taig.gandalf.core.test.{ Suite, condition, transformation }
import io.taig.gandalf.core.syntax.all._
import io.taig.gandalf.macros.obeys

class ObeysTest extends Suite {
    it should "work with case class fields" in {
        case class User( @obeys( transformation ~> condition.success ) name:String )

        //        User( "foo" )
        //        assertTypeError( """User( "" )""" )
        //        assertTypeError( """User( "   " )""" )
    }

    //    it should "infer the input type" in {
    //        case class User( @obeys( isDefined ~> trim ~> nonEmpty ) name:Option[String] )
    //
    //        User( Some( "foo" ) )
    //        assertTypeError( """User( Some( "" ) )""" )
    //        assertTypeError( """User( Some( "   " ) )""" )
    //        assertTypeError( """User( None )""" )
    //    }
}