package io.taig.gandalf.core

import cats.data.Validated._

trait Condition extends Rule.Applyable {
    override final type Output = Input

    override final def apply( input: Input )(
        implicit
        e: Error[this.type]
    ): Result[this.type] = check( input ) match {
        case true  ⇒ valid( input )
        case false ⇒ invalid( e.show( arguments( input ) ) )
    }

    def check( input: Input ): Boolean
}

object Condition {
    type Aux[T] = Condition { type Input = T }

    abstract class With[T]( f: T ⇒ Boolean ) extends Condition {
        override final type Input = T

        override final def check( input: T ) = f( input )
    }

    implicit def validationNot[C <: Condition](
        implicit
        v: Validation[C],
        e: Error[not[C]],
        a: Arguments[C]
    ): Validation[not[C]] = Validation.instance[not[C]] { input ⇒
        v.validate( input ) match {
            case Valid( _ ) ⇒
                val arguments = a.collect( input )
                val errors = e.show( arguments )
                invalid( errors )
            case Invalid( _ ) ⇒ valid( input )
        }
    }
}