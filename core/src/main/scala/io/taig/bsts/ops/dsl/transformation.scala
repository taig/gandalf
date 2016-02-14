package io.taig.bsts.ops.dsl

import io.taig.bsts.ops.hlist.NestedEvaluation
import io.taig.bsts.{ Policy, Validation }
import shapeless._

final class transformation[I, O, V <: HList, R]( a: Validation.Aux[I, O, V, R] ) {
    def ~>[P, W <: HList, S, NE <: HList]( b: Validation.Aux[O, P, W, S] )(
        implicit
        ne: NestedEvaluation.Aux[I, P, V :: Operator.~>.type :: W :: HNil, NE]
    ): Policy[I, P, V :: Operator.~>.type :: W :: HNil, NE] = {
        Policy( a.validations :: Operator.~> :: b.validations :: HNil )
    }
}