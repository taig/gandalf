package io.taig.gandalf.core

import io.taig.gandalf.core.Rule.Applyable

/**
 * Rule mixin that defines the Arguments that may be used to construct the
 * error message
 *
 * `Conditions` and `Mutations` are `Reportable`, whereas `Transformations` are
 * not because they cannot fail and hence need no error representation.
 */
trait Reportable extends Rule {
    type Arguments
}

object Reportable {
    trait With[T] extends Reportable {
        override final type Arguments = ( Input, T )
    }

    trait Input extends Applyable {
        override final type Arguments = Input

        override final def arguments( input: Input ) = input
    }

    trait None extends Applyable {
        override final type Arguments = Unit

        override final def arguments( input: Input ) = {}
    }
}