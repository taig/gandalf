package io.taig.gandalf.operation

import io.taig.gandalf._

final class dsl[L <: Rule]( val left: L ) extends AnyVal {
    def &&[R <: Rule, I, O]( right: R ): L && R = new And[L, R]

    def ||[R <: Rule, I, O]( right: R ): L || R = new Or[L, R]
}