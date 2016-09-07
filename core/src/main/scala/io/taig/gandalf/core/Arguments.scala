package io.taig.gandalf.core

import io.taig.gandalf.core.Rule.Applyable

trait Arguments[R <: Reportable] {
    def collect( input: R#Input ): R#Arguments
}

object Arguments {
    @inline
    def apply[R <: Reportable]( implicit a: Arguments[R] ): Arguments[R] = a

    def instance[R <: Reportable]( f: R#Input ⇒ R#Arguments ): Arguments[R] = {
        new Arguments[R] {
            override def collect( input: R#Input ) = f( input )
        }
    }

    def of[A <: Applyable]( f: ⇒ A ): Arguments[A] = instance[A] { input ⇒
        val applyable = f
        applyable.arguments( input.asInstanceOf[applyable.Input] )
    }
}