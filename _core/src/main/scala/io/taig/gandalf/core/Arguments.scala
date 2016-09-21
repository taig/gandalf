package io.taig.gandalf.core

import io.taig.gandalf.core.Rule.Applyable
import shapeless._

trait Arguments[R <: Rule] {
    def collect( input: R#Input ): R#Arguments
}

object Arguments {
    def apply[R <: Rule]( implicit a: Arguments[R] ): Arguments[R] = a

    def instance[R <: Rule]( f: R#Input ⇒ R#Arguments ): Arguments[R] = {
        new Arguments[R] {
            override def collect( input: R#Input ) = f( input )
        }
    }

    trait Input extends Applyable {
        override type Arguments = Input :: HNil

        override def arguments( input: Input ) = input :: HNil
    }

    trait None extends Applyable {
        override type Arguments = HNil

        override def arguments( input: Input ) = HNil
    }

    trait With[T] extends Applyable {
        override type Arguments = Input :: T :: HNil

        override def arguments( input: Input ) = input :: argument :: HNil

        def argument: T
    }
}