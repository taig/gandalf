package io.taig.bsts

import io.taig.bsts.ops.hlist.NestedEvaluation
import io.taig.bsts.syntax.dsl._
import shapeless._

class EvaluationTest extends Suite {
    it should "have a toString representation" in {
        def compute[T, V <: HList, R <: HList]( value: T, policy: Policy[T, T, V, R] )(
            implicit
            ne: NestedEvaluation.Aux[T, T, V, R]
        ) = ne( value, policy.validations )

        val ( _, computation ) = compute( "foobar", rule.required && rule.min( 3 ) && rule.max( 6 ) )
        val expected =
            """
              |Computed(
              |    Computed(
              |        Computed(Success(foobar) :: HNil) :: 
              |        && :: 
              |        Computed(Computed(Success(foobar) :: HNil) :: Computed(HNil) :: HNil) :: 
              |        HNil
              |    ) :: 
              |    && :: 
              |    Computed(
              |        Computed(Success(foobar) :: HNil) :: Computed(HNil) :: HNil
              |    ) :: 
              |    HNil
              |)
            """.stripMargin.trim.split( "\n" ).map( _.replaceFirst( "^\\s+", "" ) ).mkString( "" )

        computation.toString shouldBe expected
    }
}