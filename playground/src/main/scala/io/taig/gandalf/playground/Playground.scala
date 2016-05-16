package io.taig.gandalf.playground

import io.taig.gandalf.typelevel.Macro._
import io.taig.gandalf.typelevel._
import shapeless.Witness

import scala.language.experimental.macros
import scala.language.implicitConversions

object Playground extends App {
    implicit def implicitLifting[I <: V#Input, V <: Validation]( value: I )( implicit e: Evaluation[V] ): I Obey V = macro lift_impl[I, V]

    def lift[V <: Validation] = new LiftHelper[V]

    class LiftHelper[V <: Validation] {
        def apply[I <: V#Input]( value: I )( implicit e: Evaluation[V] ): I Obey V = macro lift_impl[I, V]
    }

    val x1 = lift[Required]( "hello" )
    val x2 = lift[Apply[IsDefined[String], Apply[Trim, Apply[ToLowerCase, And[Required, Matches[Witness.`"hello"`.T]]]]]](
        Option( "Hello      " )
    )

    case class User(
        @obeys[Apply[ToLowerCase, Required]]name: String
    )

    User( "" )

    println( u.name )
}