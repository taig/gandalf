package io.taig.gandalf.core

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
        } leftMap { e.show( input, _ ) }
    }
}

class &&[L <: Condition, R <: Condition.Aux[L#Output]]
    extends LazyAnd
    with Operator.Aux[L, R]