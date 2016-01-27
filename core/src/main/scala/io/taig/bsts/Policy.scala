package io.taig.bsts

import io.taig.bsts.ops.hlist._
import shapeless._

case class Policy[T, R <: HList]( rules: R )(
        implicit
        letp: LogicalExpressionTreePrinter[R]
) {
    private def combine[O <: Operator, S <: HList]( operator: O, policy: Policy[T, S] )(
        implicit
        letp: LogicalExpressionTreePrinter[Policy.C[R, O, S]]
    ): Policy[T, Policy.C[R, O, S]] = Policy( ( rules :: operator :: ( policy.rules :: HNil ) ) :: HNil )

    def &[S <: HList]( policy: Policy[T, S] )(
        implicit
        letp: LogicalExpressionTreePrinter[Policy.C[R, Operator.&.type, S]]
    ): Policy[T, Policy.C[R, Operator.&.type, S]] = combine( Operator.&, policy )

    def &&[S <: HList]( policy: Policy[T, S] )(
        implicit
        letp: LogicalExpressionTreePrinter[Policy.C[R, Operator.&&.type, S]]
    ): Policy[T, Policy.C[R, Operator.&&.type, S]] = combine( Operator.&&, policy )

    def |[S <: HList]( policy: Policy[T, S] )(
        implicit
        letp: LogicalExpressionTreePrinter[Policy.C[R, Operator.|.type, S]]
    ): Policy[T, Policy.C[R, Operator.|.type, S]] = combine( Operator.|, policy )

    def ||[S <: HList]( policy: Policy[T, S] )(
        implicit
        letp: LogicalExpressionTreePrinter[Policy.C[R, Operator.||.type, S]]
    ): Policy[T, Policy.C[R, Operator.||.type, S]] = combine( Operator.||, policy )

    def ^[S <: HList]( policy: Policy[T, S] )(
        implicit
        letp: LogicalExpressionTreePrinter[Policy.C[R, Operator.^.type, S]]
    ): Policy[T, Policy.C[R, Operator.^.type, S]] = combine( Operator.^, policy )

    def validate[NCM <: HList, NZ <: HList, NE <: HList]( value: T )(
        implicit
        ncm: NestedConstMapper.Aux[T, R, NCM],
        nz:  NestedZip.Aux[R, NCM, NZ],
        ne:  NestedEvaluation[NZ]
    ): Validation[Computed[ne.Out0], T] = {
        val values = ncm( value, rules )
        val zipped = nz( rules, values )

        ne( zipped ) match {
            case ( true, _ )     ⇒ Success( value )
            case ( false, tree ) ⇒ Failure( tree )
        }
    }

    override def toString = s"Policy(${letp( rules )})"
}

object Policy {
    type C[L <: HList, O <: Operator, R <: HList] = ( L :: O :: ( R :: HNil ) ) :: HNil

    //    def ¬[T, R <: HList]( policy: Policy[T, R] )(
    //        implicit
    //        letp: LogicalExpressionTreePrinter[Operator.¬.type :: R :: HNil]
    //    ): Policy[T, Operator.¬.type :: R :: HNil] = Policy( Operator.¬ :: policy.rules :: HNil )

    def apply[I <: String, T, A <: HList]( rule: Rule[I, T, A] )(
        implicit
        letp: LogicalExpressionTreePrinter[Rule[I, T, A] :: HNil]
    ): Policy[T, Rule[I, T, A] :: HNil] = Policy( rule :: HNil )
}