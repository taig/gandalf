package io.taig.gandalf.operation

import io.taig.gandalf._

final class condition[L <: Condition]( left: L ) {
    def &&[R <: Condition.Aux[L#Output]]( right: R ): L && R = new ( L && R )

    def &[R <: Condition.Aux[L#Output]]( right: R ): L & R = new ( L & R )

    def ||[R <: Condition.Aux[L#Output]]( right: R ): L || R = new ( L || R )
}