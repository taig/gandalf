package io.taig.bsts

import io.taig.bsts.rule.Required
import shapeless._

trait Fold[R <: Rule] {
    def fold( value: R#Value, rule: R ): Result[R#Value]

    def fold( result: Result[R#Value], rule: R ): Result[R#Value]
}

object Fold {
    abstract class Default[R <: Rule](
            implicit
            definition: Definition[R],
            show:       Show[R],
            adjust:     Adjust[R]
    ) extends Fold[R] {
        override def fold( value: R#Value, rule: R ) = rule.validate( value )

        override def fold( result: Result[R#Value], rule: R ) = ( result, rule.validate( result.value ) ) match {
            case ( Success( _ ), _ )                         ⇒ rule.validate( result.value )
            case ( failure @ Failure( _, _ ), Success( _ ) ) ⇒ failure
            case ( Failure( _, errors ), failure @ Failure( _, _ ) ) ⇒
                ( lens[Failure[R#Value]] >> 'errors ).modify( failure )( errors ++ _ )
        }
    }

    implicit def `Fold[Rule]`[R <: Rule](
        implicit
        definition: Definition[R],
        show:       Show[R],
        adjust:     Adjust[R],
        empty:      Empty[R]
    ) = new Default[R] {
        override def fold( value: R#Value, rule: R ) = empty.isEmpty( value ) match {
            case true  ⇒ Success( value )
            case false ⇒ super.fold( value, rule )
        }

        override def fold( result: Result[R#Value], rule: R ) = empty.isEmpty( result.value ) match {
            case true  ⇒ result
            case false ⇒ super.fold( result, rule )
        }
    }

    implicit def `Fold[Required]`[T](
        implicit
        definition: Definition[Required[T]],
        show:       Show[Required[T]],
        adjust:     Adjust[Required[T]]
    ) = new Default[Required[T]] {}
}