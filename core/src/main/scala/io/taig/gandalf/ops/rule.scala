package io.taig.gandalf.ops

import io.taig.gandalf.Rule
import io.taig.gandalf.operator.{ EagerAnd, LazyAnd }

class rule[T, L <: Rule.Aux[T]]( left: L ) {
    def &&[R <: Rule.Aux[T]]( right: R ): L LazyAnd R = new LazyAnd

    def &[R <: Rule.Aux[T]]( right: R ): L EagerAnd R = new EagerAnd
}