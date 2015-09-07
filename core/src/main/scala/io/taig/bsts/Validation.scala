package io.taig.bsts

import io.taig.bsts.Validation.combine
import io.taig.bsts.rule.Required
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
        implicit def head[R <: Rule](
            implicit
            definition: Definition[R],
            show:       Show[R],
            adjust:     Adjust[R],
            empty:      Empty[R]
        ): Case.Aux[R#Value, R, Result[R#Value]] = {
            at[R#Value, R]( ( value, rule ) ⇒ !rule.isInstanceOf[Required[_]] && empty.isEmpty( value ) match {
                case true  ⇒ Success( value )
                case false ⇒ rule.validate( value )
            } )
        }

        implicit def tail[T, R <: Rule.Aux[T]](
            implicit
            definition: Definition[R],
            show:       Show[R],
            adjust:     Adjust[R],
            empty:      Empty[R]
        ): Case.Aux[Result[T], R, Result[T]] = {
            at[Result[T], R]( ( result, rule ) ⇒ !rule.isInstanceOf[Required[_]] && empty.isEmpty( result.value ) match {
                case true  ⇒ result
                case false ⇒ rule.validate( result.value )
            } )
        }
    }
}