package io.taig.gandalf.macros.test

import io.taig.gandalf.core.test._
import io.taig.gandalf.core.syntax.all._
import io.taig.gandalf.macros.obeys
import io.taig.gandalf.macros.implicits._

class ObeysTest extends Suite {
    it should "work with case class fields" in {
        case class User( @obeys( transformation ~> condition.success ) name:String )
        User( "foo" )

        case class Address( @obeys( transformation ~> condition.failure ) street:String )
        assertTypeError( """Address( "foo" )""" )
    }

    it should "infer the input type" in {
        case class User( @obeys( generic.instance ~> condition.success ) name:String )
        User( "foo" )
    }
}