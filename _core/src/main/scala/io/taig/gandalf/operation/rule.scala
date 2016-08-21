package io.taig.gandalf.operation

import io.taig.gandalf.data.{ EagerAnd, LazyAnd, Or, Rule }
import io.taig.gandalf.syntax.aliases._

final class rule[T, L <: Rule.Aux[T]]( left: L ) {
    def &[R <: Rule.Aux[T]]( right: R ): EagerAnd.Aux[L, R] = new &[L, R]

    def &&[R <: Rule.Aux[T]]( right: R ): LazyAnd.Aux[L, R] = new &&[L, R]

    def ||[R <: Rule.Aux[T]]( right: R ): Or.Aux[L, R] = new ||[L, R]
}