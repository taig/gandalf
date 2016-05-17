package io.taig.gandalf.playground

import io.taig.gandalf.typelevel.Required.required
import io.taig.gandalf.typelevel.Trim.trim
import io.taig.gandalf.typelevel._
import io.taig.gandalf.typelevel.annotation.obeys
import shapeless.Witness

import scala.language.experimental.macros
import scala.language.implicitConversions

object Playground extends App {
    import syntax.all._

    val x1 = lift[Required && Required]( "something" )
    val x2 = lift[Apply[IsDefined[String], Apply[Trim, Required]]](
        Option( "   something   " )
    )

    println( x1 )
    println( x2 )

    case class User(
        @obeys[Apply[ToLowerCase, Required]]name: String
    )

    case class Experimental(
        @obeys( trim ~> required ) name:String
    )

    val x3 = Experimental( "  f" )

    println( x3 )

    val x4 = lift[Regex[Witness.`"me.*"`.T]]( "me@web.de" )

    println( x4 )
}