package io.taig.bsts

import io.taig.bsts.syntax.all._
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

object Error {
    def apply[A <: HList]( identifier: String, arguments: A )(
        implicit
        tt: ToTraversable.Aux[A, List, Any]
    ): Error[identifier.type, A] = Error( arguments )( Witness.mkWitness( identifier ), tt )
}