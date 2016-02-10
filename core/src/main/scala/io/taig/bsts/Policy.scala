package io.taig.bsts

import io.taig.bsts.ops.hlist.{ Printer, NestedEvaluation }
import io.taig.bsts
import shapeless._

abstract class Policy[I, O, V <: HList, R <: HList]( val validations: V )(
        implicit
        ne: NestedEvaluation.Aux[I, O, V, R]
) extends Validation[I, O] {
    override type F = Computed[R]

    override def validate( input: I ) = ne( input, validations ) match {
        case ( Some( output ), _ ) ⇒ Success( output )
        case ( None, evaluation )  ⇒ Failure( evaluation )
    }
}

object Policy {
    def apply[N <: String, T, A <: HList, R <: HList](
        rule: bsts.Rule[N, T, A]
    )(
        implicit
        ne: NestedEvaluation.Aux[T, T, bsts.Rule[N, T, A] :: HNil, R]
    ): Rule[T, bsts.Rule[N, T, A] :: HNil, R] = Rule( rule :: HNil )

    case class Rule[T, V <: HList, R <: HList]( rules: V )(
            implicit
            ne: NestedEvaluation.Aux[T, T, V, R],
            p:  Printer[V]
    ) extends Policy[T, T, V, R]( rules ) {
        override def toString = s"Policy.Rule(${p( true, rules )})"
    }

    def apply[N <: String, I, O, A <: HList, R <: HList](
        transformation: bsts.Transformation[N, I, O, A]
    )(
        implicit
        ne: NestedEvaluation.Aux[I, O, bsts.Transformation[N, I, O, A] :: HNil, R]
    ): Transformation[I, O, bsts.Transformation[N, I, O, A] :: HNil, R] = Transformation( transformation :: HNil )

    case class Transformation[I, O, V <: HList, R <: HList]( transformations: V )(
            implicit
            ne: NestedEvaluation.Aux[I, O, V, R],
            p:  Printer[V]
    ) extends Policy[I, O, V, R]( transformations ) {
        override def toString = s"Policy.Transformation(${p( true, transformations )})"
    }
}