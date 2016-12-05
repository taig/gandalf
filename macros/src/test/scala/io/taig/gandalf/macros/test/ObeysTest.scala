//package io.taig.gandalf.macros.test
//
//import io.taig.gandalf.core.test._
//import io.taig.gandalf.core.goaway.syntax.all._
//import io.taig.gandalf.macros.obeys
//import io.taig.gandalf.macros.implicits._
//
//class ObeysTest extends Suite {
//    it should "support case class fields" in {
//        case class User( @obeys( transformation && condition.success ) name:String )
//        User( "foo" )
//
//        case class Address( @obeys( transformation && condition.failure ) street:String )
//        assertTypeError( """Address( "foo" )""" )
//    }
//
//    it should "support class fields" in {
//        class User( @obeys( transformation && condition.success ) name:String )
//        new User( "foo" )
//
//        class Address( @obeys( transformation && condition.failure ) street:String )
//        assertTypeError( """new Address( "foo" )""" )
//    }
//
//    it should "support val definitions" in {
//        @obeys( transformation && condition.success )
//        val name: String = "foo"
//
//        assertTypeError {
//            """@obeys( transformation && condition.failure ) val street: String = "foo""""
//        }
//    }
//
//    it should "support var definitions" in {
//        @obeys( transformation && condition.success )
//        var name: String = "foo"
//
//        assertTypeError {
//            """@obeys( transformation && condition.failure ) var street: String = "foo""""
//        }
//    }
//
//    it should "support def definitions" in {
//        @obeys( transformation && condition.success )
//        def name: String = "foo"
//
//        assertTypeError {
//            """@obeys( transformation && condition.failure ) def street: String = "foo""""
//        }
//    }
//
//    it should "support def parameters" in {
//        // def test( @obeys( transformation && condition.success ) yolo:String ) = "foo"
//    }
//
//    it should "infer the input type" in {
//        case class User( @obeys( generic.instance && condition.success ) name:String )
//        User( "foo" )
//    }
//}