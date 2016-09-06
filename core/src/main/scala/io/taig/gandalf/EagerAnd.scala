package io.taig.gandalf

import cats.syntax.cartesian._

class EagerAnd
        extends Operator
        with Condition {
    override type Left <: Condition

    override type Right <: Condition.Aux[Left#Output]
}

object EagerAnd {
    implicit def validation[A <: EagerAnd](
        implicit
        l: Validation[A#Left],
        r: Validation[A#Right],
        e: Error[A]
    ): Validation[A] = Validation.instance[A] { input ⇒
        val left = l.validate( input )
        val right = r.validate( input.asInstanceOf[A#Right#Input] )
        ( left |@| right )
            .map( ( _, _ ) ⇒ input )
            .leftMap( e.show( input, _ ) )
    }
}

class &[L <: Condition, R <: Condition.Aux[L#Output]]
    extends EagerAnd
    with Operator.Aux[L, R]