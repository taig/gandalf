package io.taig.gandalf.core

import shapeless.HNil

class LazyAnd extends Operator.Logical

object LazyAnd {
    implicit def validation[A <: LazyAnd](
        implicit
        l: Validation[A#Left],
        r: Validation[A#Right],
        e: Error[A]
    ): Validation[A] = Validation.instance[A] { input ⇒
        l.validate( input ) andThen { _ ⇒
            r.validate( input.asInstanceOf[A#Right#Input] )
        } leftMap { errors ⇒
            e.show( input :: errors :: HNil )
        }
    }

    implicit def serialization[A <: LazyAnd](
        implicit
        l: Serialization[A#Left],
        r: Serialization[A#Right]
    ): Serialization[A] = {
        Serialization.instance( s"(${l.serialize} && ${r.serialize})" )
    }
}

class &&[L <: Condition, R <: Condition.Aux[L#Output]]
    extends LazyAnd
    with Operator.Aux[L, R]