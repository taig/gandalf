package io.taig.gandalf.core

import io.taig.gandalf.core.Rule.Applyable

trait Validation[R <: Rule] {
    def validate( input: R#Input ): Result[R]
}

object Validation {
    @inline
    def apply[R <: Rule]( implicit v: Validation[R] ): Validation[R] = v

    def instance[R <: Rule](
        f: R#Input ⇒ Result[R]
    ): Validation[R] = new Validation[R] {
        override def validate( input: R#Input ) = f( input )
    }

    def of[A <: Applyable]( f: ⇒ A )(
        implicit
        e: Error[A]
    ): Validation[A] = instance[A] { input ⇒
        val applyable = f
        applyable( input.asInstanceOf[applyable.Input] )
    }
}