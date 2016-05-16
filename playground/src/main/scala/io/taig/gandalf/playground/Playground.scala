package io.taig.gandalf.playground

import io.taig.gandalf.typelevel.Macro._
import io.taig.gandalf.typelevel._
import io.taig.gandalf.typelevel.annotation.obeys
import shapeless.Witness

import scala.language.experimental.macros
import scala.language.implicitConversions

object Playground extends App {
    val x1 = lift[And[Required, Required]]( "" )
    //    val x2 = lift[Apply[IsDefined[String], Apply[Trim, Apply[ToLowerCase, And[Required, Matches[Witness.`"hello"`.T]]]]]](
    //        Option( "Hello      " )
    //    )

    println( x1 )

    case class User(
        @obeys[Apply[ToLowerCase, Required]]name: String
    )
}