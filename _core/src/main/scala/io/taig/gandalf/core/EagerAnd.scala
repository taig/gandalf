package io.taig.gandalf.core

import cats.syntax.cartesian._
import shapeless.HNil

class EagerAnd extends Operator.Logical

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
            .leftMap { errors ⇒
                e.show( input :: errors :: HNil )
            }
    }

    implicit def serialization[A <: EagerAnd](
        implicit
        l: Serialization[A#Left],
        r: Serialization[A#Right]
    ): Serialization[A] = {
        Serialization.instance( s"(${l.serialize} & ${r.serialize})" )
    }
}

class &[L <: Condition, R <: Condition.Aux[L#Output]]
    extends EagerAnd
    with Operator.Aux[L, R]