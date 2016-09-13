package io.taig.gandalf.core.operation

import io.taig.gandalf.core._

final class mutation[L <: Mutation]( left: L ) {
    def ~>[R <: Rule.Input[L#Output]]( right: R ): L ~> R = new ( L ~> R )
}