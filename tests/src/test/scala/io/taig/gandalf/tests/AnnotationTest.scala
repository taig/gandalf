package io.taig.gandalf.tests

import io.taig.gandalf._
import io.taig.gandalf.data.Transform
import io.taig.gandalf.internal.Identity
import io.taig.gandalf.syntax.all._
import io.taig.gandalf.predef._
import io.taig.gandalf.predef.messages._

class AnnotationTest extends Suite {
    "@obeys" should "work with case class fields" in {
        case class User( @obeys( Required ) name:String )

        User( lift[Required]( "taig" ) )
    }
}