package io.taig.gandalf.ops

import io.taig.gandalf.Rule
import io.taig.gandalf.operation.{ EagerAnd, LazyAnd, Or }
import io.taig.gandalf.syntax.aliases._

final class rule[T, L <: Rule.Aux[T]]( left: L ) {
    def &[R <: Rule.Aux[T]]( right: R ): L & R = new EagerAnd

    def &&[R <: Rule.Aux[T]]( right: R ): L && R = new LazyAnd

    def ||[R <: Rule.Aux[T]]( right: R ): L || R = new Or
}