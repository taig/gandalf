package io.taig.gandalf.operation

import io.taig.gandalf._

final case class mutation[L <: Mutation]( left: L ) {
    def ~>[R <: Validatable.Input[L#Output]]( right: R ) = {
        new Mutate {
            override type Left = L

            override type Right = R
        }
    }
}