package io.taig.gandalf

import cats.data.Validated.{ Invalid, Valid }
import io.taig.gandalf.ops.NestedEvaluation
import shapeless.HList

case class Policy[I, O, V0 <: HList, NE <: HList]( validations: V0 )(
        implicit
        ne: NestedEvaluation.Aux[I, O, V0, NE]
) extends Validation[I, O] {
    override type V = V0

    override type E = NE

    override def validate( input: I ) = ne.apply( input, validations ) match {
        case ( Some( output ), _ ) ⇒ Valid( output )
        case ( None, evaluation )  ⇒ Invalid( evaluation )
    }

    override def toString = s"Policy($validations)"
}