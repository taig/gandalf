package io.taig.bsts.predef

import cats.data.Validated.Valid
import io.taig.bsts.Term
import io.taig.bsts.ops.hlist.NestedEvaluation
import shapeless._

abstract class Transformation[N <: String, I, O](
        implicit
        w: Witness.Aux[N]
) extends Term[N, I, O, HNil] {
    override final type V = Transformation[N, I, O] :: HNil

    override final type E = Nothing

    override final def validations: V = this :: HNil

    override def validate( input: I ): Valid[O]
}

object Transformation {
    def apply[I, O]( name: String )( f: I ⇒ O )(
        implicit
        w: Witness.Aux[name.type]
    ): Transformation[name.type, I, O] = new Transformation[name.type, I, O] {
        override def validate( input: I ) = Valid( f( input ) )
    }

    implicit def nestedEvaluationTransformation[N <: String, I, O, A <: HList] = {
        new NestedEvaluation[I, O, Transformation[N, I, O] :: HNil] {
            override type Out0 = Valid[O] :: HNil

            override def apply( input: I, tree: Transformation[N, I, O] :: HNil ) = tree match {
                case transformation :: HNil ⇒
                    val output = transformation.validate( input ).a
                    ( Some( output ), Valid( output ) :: HNil )
            }
        }
    }
}