package io.taig.gandalf.core.operation

import io.taig.gandalf.core._

final class operation[L <: Rule, I, O]( val left: L ) extends AnyVal {
    def &&[R <: Rule]( right: R ): L && R = new And[L, R]

    def ||[R <: Rule]( right: R ): L || R = new Or[L, R]
}