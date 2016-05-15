package io.taig.gandalf.playground

import io.taig.gandalf.typelevel.Macro._
import io.taig.gandalf.typelevel._

import scala.language.experimental.macros
import scala.language.implicitConversions

object Playground extends App {
    implicit def convert[L, R <: Validation.In[L]]( obeys: Obeys[L, R] ): R#Output = obeys.value

    case class User( name: String Obeys Apply[Trim, Required] )

    def lift[V <: Validation]( value: V#Input )(
        implicit
        e: Evaluation[V]
    ): V#Input Obeys V = macro lift_impl[V]

    val l0 = lift[Apply[Trim, Required]]( "          asdf   " )
    //    val l1 = lift[Required]( "" )
    //    val l2 = lift[Apply[Trim, Required]]( "   " )

    println( l0 )

}