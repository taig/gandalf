package io.taig.gandalf.core.operation

import io.taig.gandalf.core._

final class dsl[L <: Rule]( val left: L ) extends AnyVal {
    def &&[R <: Rule]( right: R )(
        implicit
        r: Resolver[L && R]
    ): L && R = new And[L, R]

    def ||[R <: Rule]( right: R )(
        implicit
        r: Resolver[L || R]
    ): L || R = new Or[L, R]
}