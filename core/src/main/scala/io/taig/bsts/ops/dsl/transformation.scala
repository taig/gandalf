package io.taig.bsts.ops.dsl

import io.taig.bsts.ops.NestedEvaluation
import io.taig.bsts.{ Policy, Validation }
import shapeless._

class transformation[I, O, V <: HList, E]( a: Validation.Aux[I, O, V, E] ) {
    def ~>[P, W <: HList, F, NE <: HList]( b: Validation.Aux[O, P, W, F] )(
        implicit
        ne: NestedEvaluation.Aux[I, P, V :: Operator.~>.type :: W :: HNil, NE]
    ): Policy[I, P, V :: Operator.~>.type :: W :: HNil, NE] = {
        Policy( a.validations :: Operator.~> :: b.validations :: HNil )
    }
}