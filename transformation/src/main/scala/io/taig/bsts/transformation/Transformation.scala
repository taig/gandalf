package io.taig.bsts.transformation

import cats.data.Validated.Valid
import io.taig.bsts.Term
import io.taig.bsts.ops.hlist.NestedEvaluation
import shapeless._

abstract class Transformation[N <: String, I, O](
        implicit
        w: Witness.Aux[N]
) extends Term[N, I, O, HNil] {
    override final type V = Transformation[N, I, O] :: HNil

    override final type R = O

    override final def validations: V = this :: HNil
}

object Transformation {
    def apply[I, O]( name: String )( f: I ⇒ O )(
        implicit
        w: Witness.Aux[name.type]
    ): Transformation[name.type, I, O] = new Transformation[name.type, I, O] {
        override def validate( input: I ) = f( input )
    }

    implicit def nestedEvaluationTransformation[N <: String, I, O, A <: HList] = {
        new NestedEvaluation[I, O, Transformation[N, I, O] :: HNil] {
            override type Out0 = Valid[O] :: HNil

            override def apply( input: I, tree: Transformation[N, I, O] :: HNil ) = tree match {
                case transformation :: HNil ⇒
                    val output = transformation.validate( input )
                    ( Some( output ), Valid( output ) :: HNil )
            }
        }
    }
}