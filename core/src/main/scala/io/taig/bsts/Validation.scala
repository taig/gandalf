package io.taig.bsts

import io.taig.bsts.Validation.combine
import shapeless._
import shapeless.ops.hlist.LeftFolder

case class Validation[T, H <: HList]( rules: H )(
        implicit
        folder: LeftFolder.Aux[H, T, combine.type, Result[T]]
) {
    def validate( value: T ): Result[T] = rules.foldLeft( value )( combine )
}

object Validation {
    def apply[T, R <: Rule]( rule: R )(
        implicit
        folder: LeftFolder.Aux[R :: HNil, T, combine.type, Result[T]]
    ): Validation[T, R :: HNil] = {
        new Validation[T, R :: HNil]( rule :: HNil )
    }

    object combine extends Poly2 {
        implicit def head[R <: Rule]( implicit fold: Fold[R] ): Case.Aux[R#Value, R, Result[R#Value]] = {
            at[R#Value, R]( fold.fold )
        }

        implicit def tail[T, R <: Rule.Aux[T]]( implicit fold: Fold[R] ): Case.Aux[Result[T], R, Result[T]] = {
            at[Result[T], R]( fold.fold )
        }
    }
}