package io.taig.gandalf.operation

import io.taig.gandalf.data.{ EagerAnd, LazyAnd, Or, Rule }
import io.taig.gandalf.syntax.aliases._

final class rule[T, L <: Rule.Aux[T]]( left: L ) {
    def &[R <: Rule.Aux[T]]( right: R ): L & R = new EagerAnd

    def &&[R <: Rule.Aux[T]]( right: R ): L && R = new LazyAnd

    def ||[R <: Rule.Aux[T]]( right: R ): L || R = new Or
}