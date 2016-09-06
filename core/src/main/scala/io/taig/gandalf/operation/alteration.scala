package io.taig.gandalf.operation

import io.taig.gandalf._

final class alteration[L <: Alteration]( left: L ) {
    def ~>[R <: Rule.Input[L#Output]]( right: R ): L ~> R = new ( L ~> R )
}