package io.taig.bsts.ops.dsl

import io.taig.bsts.ops.hlist.NestedEvaluation
import io.taig.bsts.{ Policy, Validation }
import shapeless._

final class logic[I, O, V <: HList, R]( a: Validation.Aux[I, O, V, R] ) {
    def &[W <: HList, S, NE <: HList]( b: Validation.Aux[O, O, W, S] )(
        implicit
        ne: NestedEvaluation.Aux[I, O, V :: Operator.&.type :: W :: HNil, NE]
    ): Policy[I, O, V :: Operator.&.type :: W :: HNil, NE] = {
        Policy( a.validations :: Operator.& :: b.validations :: HNil )
    }

    def &&[W <: HList, S, NE <: HList]( b: Validation.Aux[O, O, W, S] )(
        implicit
        ne: NestedEvaluation.Aux[I, O, V :: Operator.&&.type :: W :: HNil, NE]
    ): Policy[I, O, V :: Operator.&&.type :: W :: HNil, NE] = {
        Policy( a.validations :: Operator.&& :: b.validations :: HNil )
    }

    def |[W <: HList, S, NE <: HList]( b: Validation.Aux[O, O, W, S] )(
        implicit
        ne: NestedEvaluation.Aux[I, O, V :: Operator.|.type :: W :: HNil, NE]
    ): Policy[I, O, V :: Operator.|.type :: W :: HNil, NE] = {
        Policy( a.validations :: Operator.| :: b.validations :: HNil )
    }

    def ||[W <: HList, S, NE <: HList]( b: Validation.Aux[O, O, W, S] )(
        implicit
        ne: NestedEvaluation.Aux[I, O, V :: Operator.||.type :: W :: HNil, NE]
    ): Policy[I, O, V :: Operator.||.type :: W :: HNil, NE] = {
        Policy( a.validations :: Operator.|| :: b.validations :: HNil )
    }

    def ^[W <: HList, S, NE <: HList]( b: Validation.Aux[O, O, W, S] )(
        implicit
        ne: NestedEvaluation.Aux[I, O, V :: Operator.^.type :: W :: HNil, NE]
    ): Policy[I, O, V :: Operator.^.type :: W :: HNil, NE] = {
        Policy( a.validations :: Operator.^ :: b.validations :: HNil )
    }
}