package io.taig.bsts

import io.taig.bsts.ops.hlist.{ NestedEvaluation, NestedZip, NestedConstMapper }
import shapeless._

class EvaluationTest extends Suite {
    it should "have a useful toString representation" in {
        def compute[T, R <: HList, NCM <: HList, NZ <: HList]( value: T, policy: Policy[T, R] )(
            implicit
            ncm: NestedConstMapper.Aux[T, R, NCM],
            nz:  NestedZip.Aux[R, NCM, NZ],
            ne:  NestedEvaluation[NZ]
        ) = {
            val values = ncm( value, policy.rules )
            val zipped = nz( policy.rules, values )
            ne( zipped )
        }

        val ( _, computation ) = compute( "foobar", rule.required && rule.min( 8 ) )
        computation.toString shouldBe
            "Computed(Computed(Computed(Success(foobar) :: HNil) :: && :: " +
            "Right(Computed(Computed(Failure(Error(min, (foobar, 8, 6))) :: HNil) :: HNil)) :: HNil) :: HNil)"
    }
}