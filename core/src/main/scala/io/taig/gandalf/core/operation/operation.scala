package io.taig.gandalf.core.operation

import io.taig.gandalf.core._

final class operation[L, I, O]( val left: L ) extends AnyVal {
    def &&[R]( right: R ): L && R = new And[L, R]

    def ||[R]( right: R ): L || R = new Or[L, R]
}