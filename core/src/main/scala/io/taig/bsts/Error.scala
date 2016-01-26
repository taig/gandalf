package io.taig.bsts

import io.taig.bsts.syntax.ErrorOps
import shapeless._
import shapeless.ops.hlist.ToTraversable

import scala.language.implicitConversions

final case class Error[I <: String, A <: HList]( arguments: A )(
        implicit
        w:  Witness.Aux[I],
        tt: ToTraversable.Aux[A, List, Any]
) {
    def raw: ( String, List[Any] ) = ( w.value, arguments.toList )

    override def toString = {
        val arguments = this.arguments.toList match {
            case Nil       ⇒ ""
            case arguments ⇒ s" (${arguments.mkString( ", " )})"
        }

        w.value + arguments
    }
}

object Error {
    def apply[A <: HList]( identifier: String, arguments: A )(
        implicit
        tt: ToTraversable.Aux[A, List, Any]
    ): Error[identifier.type, A] = Error( arguments )( Witness.mkWitness( identifier ), tt )

    implicit def syntax[I <: String, A <: HList]( error: Error[I, A] ): ErrorOps[I, A] = new ErrorOps[I, A]( error )
}