//package io.taig.gandalf.tests
//
//import io.taig.gandalf._
//import io.taig.gandalf.predef.IsDefined.isDefined
//import io.taig.gandalf.predef.Required.required
//import io.taig.gandalf.predef.Trim.trim
//import io.taig.gandalf.syntax.all._
//import io.taig.gandalf.predef.messages._
//
//class AnnotationTest extends Suite {
//    "@obeys" should "work with case class fields" in {
//        case class User( @obeys( trim ~> required ) name:String )
//
//        User( "foo" )
//        assertTypeError( """User( "" )""" )
//        assertTypeError( """User( "   " )""" )
//    }
//
//    it should "infer the input type" in {
//        case class User( @obeys( isDefined ~> trim ~> required ) name:Option[String] )
//
//        User( Some( "foo" ) )
//        assertTypeError( """User( Some( "" ) )""" )
//        assertTypeError( """User( Some( "   " ) )""" )
//        assertTypeError( """User( None )""" )
//    }
//}