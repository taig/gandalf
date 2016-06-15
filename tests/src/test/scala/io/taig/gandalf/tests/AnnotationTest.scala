package io.taig.gandalf.tests

import io.taig.gandalf._
import io.taig.gandalf.predef.IsDefined.isDefined
import io.taig.gandalf.syntax.all._
import io.taig.gandalf.predef._
import io.taig.gandalf.predef.messages._

class AnnotationTest extends Suite {
    "@obeys" should "work with case class fields" in {
        case class User( @obeys( isDefined[String] ) name:Option[String] )

        User( Some( "taig" ) )
    }
}