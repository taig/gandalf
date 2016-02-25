package io.taig.gandalf.ops.dsl

import io.taig.gandalf.ops.NestedEvaluation
import io.taig.gandalf.{ Policy, Validation }
import shapeless._

class logic[I, O, V <: HList, E]( a: Validation.Aux[I, O, V, E] ) {
    def &[W <: HList, F, NE <: HList]( b: Validation.Aux[O, O, W, F] )(
        implicit
        ne: NestedEvaluation.Aux[I, O, V :: Operator.&.type :: W :: HNil, NE]
    ): Policy[I, O, V :: Operator.&.type :: W :: HNil, NE] = {
        Policy( a.validations :: Operator.& :: b.validations :: HNil )
    }

    def &&[W <: HList, F, NE <: HList]( b: Validation.Aux[O, O, W, F] )(
        implicit
        ne: NestedEvaluation.Aux[I, O, V :: Operator.&&.type :: W :: HNil, NE]
    ): Policy[I, O, V :: Operator.&&.type :: W :: HNil, NE] = {
        Policy( a.validations :: Operator.&& :: b.validations :: HNil )
    }

    def |[W <: HList, F, NE <: HList]( b: Validation.Aux[O, O, W, F] )(
        implicit
        ne: NestedEvaluation.Aux[I, O, V :: Operator.|.type :: W :: HNil, NE]
    ): Policy[I, O, V :: Operator.|.type :: W :: HNil, NE] = {
        Policy( a.validations :: Operator.| :: b.validations :: HNil )
    }

    def ||[W <: HList, F, NE <: HList]( b: Validation.Aux[O, O, W, F] )(
        implicit
        ne: NestedEvaluation.Aux[I, O, V :: Operator.||.type :: W :: HNil, NE]
    ): Policy[I, O, V :: Operator.||.type :: W :: HNil, NE] = {
        Policy( a.validations :: Operator.|| :: b.validations :: HNil )
    }

    def ^[W <: HList, F, NE <: HList]( b: Validation.Aux[O, O, W, F] )(
        implicit
        ne: NestedEvaluation.Aux[I, O, V :: Operator.^.type :: W :: HNil, NE]
    ): Policy[I, O, V :: Operator.^.type :: W :: HNil, NE] = {
        Policy( a.validations :: Operator.^ :: b.validations :: HNil )
    }
}