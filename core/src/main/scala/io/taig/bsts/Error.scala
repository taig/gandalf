package io.taig.bsts

import shapeless._
import shapeless.ops.hlist.ToTraversable

import scala.language.implicitConversions

final case class Error[I <: String, A <: HList]( arguments: A )(
        implicit
        w:  Witness.Aux[I],
        tt: ToTraversable.Aux[A, List, Any]
) {
    override def toString = {
        val arguments = this.arguments.toList match {
            case Nil       ⇒ ""
            case arguments ⇒ s", (${arguments.mkString( ", " )})"
        }

        s"Error(${w.value}$arguments)"
    }
}