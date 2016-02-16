package io.taig.bsts

import cats.data.Xor
import cats.data.Xor._
import io.taig.bsts.ops.Computed
import io.taig.bsts.ops.hlist.{ NestedEvaluation, Printer }
import shapeless.HList

case class Policy[I, O, V0 <: HList, NE <: HList]( validations: V0 )(
        implicit
        ne: NestedEvaluation.Aux[I, O, V0, NE],
        p:  Printer[V0]
) extends Validation[I, O] {
    override type V = V0

    override type R = Xor[Computed[NE], O]

    override def validate( input: I ) = ne.apply( input, validations ) match {
        case ( Some( output ), _ ) ⇒ Right( output )
        case ( None, evaluation )  ⇒ Left( evaluation )
    }

    override def toString = s"Policy(${p( true, validations )})"
}