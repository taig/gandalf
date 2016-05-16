package io.taig.gandalf.playground

import io.taig.gandalf.typelevel.Macro._
import io.taig.gandalf.typelevel.Matches.matches
import io.taig.gandalf.typelevel.Required.required
import io.taig.gandalf.typelevel._
import io.taig.gandalf.typelevel.annotation.{ obeys, obeysFancy }
import shapeless.Witness

import scala.language.experimental.macros
import scala.language.implicitConversions

object Playground extends App {
    import syntax.all._

    val x1 = lift[&&[Required, Required]]( "something" )
    val x2 = lift[Apply[IsDefined[String], Apply[Trim, Required]]](
        Option( "   something   " )
    )

    println( x1 )
    println( x2 )

    case class User(
        @obeys[Apply[ToLowerCase, Required]]name: String
    )

    case class Experimental(
        @obeysFancy( required && required ) name:String
    )

    val x3 = lift[Matches[Int, Witness.`3`.T]]( 3 )

    val z = matches( "asdf" )

    &&( z, required )
    &&( required, z )

    required && required
    z && required

    println( x3 )
}