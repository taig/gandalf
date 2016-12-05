package io.taig.gandalf.core.operation

import io.taig.gandalf.core._

final class dsl[L <: Rule]( val left: L ) extends AnyVal {
    def &&[R <: Rule, I, O]( right: R ): L && R = new And[L, R]

    def ||[R <: Rule, I, O]( right: R ): L || R = new Or[L, R]
}