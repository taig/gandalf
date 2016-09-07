package io.taig.gandalf.core.operation

import io.taig.gandalf.core._

final class condition[L <: Condition]( left: L ) {
    def &&[R <: Condition.Aux[L#Output]]( right: R ): L && R = new ( L && R )

    def &[R <: Condition.Aux[L#Output]]( right: R ): L & R = new ( L & R )

    def ||[R <: Condition.Aux[L#Output]]( right: R ): L || R = new ( L || R )
}