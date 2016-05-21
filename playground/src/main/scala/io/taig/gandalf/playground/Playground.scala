package io.taig.gandalf.playground

import io.taig.gandalf.predef.Required.required
import io.taig.gandalf.predef.Trim.trim
import io.taig.gandalf.predef._
import io.taig.gandalf._
import io.taig.gandalf.internal.Identity
import io.taig.gandalf.operator.{ Apply, LazyAnd }
import io.taig.gandalf.predef.IsDefined.isDefined
import shapeless.Witness

import scala.language.experimental.macros
import scala.language.implicitConversions

object Playground extends App {
    import io.taig.gandalf.syntax.all._

    val x1 = lift[Required && Required]( "something" )
    val x2 = lift[Apply[IsDefined[String], Apply[Trim, LazyAnd[Required, Required]]]](
        Option( "  fd    " )
    )

    println( x1 )
    println( x2 )

    Identity.identity[String] ~> required

    case class User(
        @obeys[Apply[ToLowerCase, Required]]name: String
    )

    case class Experimental(
        @obeys( isDefined ~> trim ~> required ) name:Option[String]
    )

    val x3 = Experimental( Option( "yolo" ) )

    println( x3 )
}