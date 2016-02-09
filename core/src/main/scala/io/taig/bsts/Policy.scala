package io.taig.bsts

import io.taig.bsts.ops.hlist.{ Printer, NestedEvaluation }
import io.taig.bsts
import shapeless._

abstract class Policy[I, O, V <: HList]( val validations: V )(
        implicit
        p: Printer[V]
) extends Validation[I, O] {
    def validate( value: I )(
        implicit
        ne: NestedEvaluation[I, O, V]
    ): Result[Computed[ne.Out0], O] = ne( value, validations ) match {
        case ( Some( output ), _ ) ⇒ Success( output )
        case ( None, evaluation )  ⇒ Failure( evaluation )
    }
}

object Policy {
    def apply[N <: String, T, A <: HList](
        rule: bsts.Rule[N, T, A]
    ): Rule[T, bsts.Rule[N, T, A] :: HNil] = Rule( rule :: HNil )

    case class Rule[T, R <: HList]( rules: R )(
            implicit
            p: Printer[R]
    ) extends Policy[T, T, R]( rules ) {
        override def toString = s"Policy.Rule(${p( true, rules )})"
    }

    def apply[N <: String, I, O, A <: HList](
        transformation: bsts.Transformation[N, I, O, A]
    ): Transformation[I, O, bsts.Transformation[N, I, O, A] :: HNil] = Transformation( transformation :: HNil )

    case class Transformation[I, O, T <: HList]( transformations: T )(
            implicit
            p: Printer[T]
    ) extends Policy[I, O, T]( transformations ) {
        override def toString = s"Policy.Transformation(${p( true, transformations )})"
    }
}