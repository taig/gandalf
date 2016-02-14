package io.taig.bsts.transformation

import io.taig.bsts.Term
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
}