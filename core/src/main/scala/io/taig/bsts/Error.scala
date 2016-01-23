package io.taig.bsts

import shapeless._
import shapeless.ops.hlist.ToTraversable

final case class Error[I <: String, A <: HList]( arguments: A )(
        implicit
        w:  Witness.Aux[I],
        tt: ToTraversable.Aux[A, List, Any]
) extends Validation[Report[I, A], String, ( String, List[Any] )] {
    override def isSuccess = false

    override def report( implicit r: Report[I, A] ): String = r.report( this )

    override def raw: ( String, List[Any] ) = ( w.value, arguments.toList )

    override def toString = w.value + arguments.toList.mkString( " (", ", ", ")" )
}

object Error {
    def apply[A <: HList]( identifier: String, arguments: A )(
        implicit
        tt: ToTraversable.Aux[A, List, Any]
    ): Error[identifier.type, A] = Error( arguments )( Witness.mkWitness( identifier ), tt )
}