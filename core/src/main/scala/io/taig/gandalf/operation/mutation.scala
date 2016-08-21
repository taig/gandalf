package io.taig.gandalf.operation

import io.taig.gandalf.{ Mutate, Mutation, Validatable }

final class mutation[L <: Mutation] {
    def ~>[R <: Validatable.Input[L#Output]]( right: R ): Mutate.Aux[L, R] = {
        new Mutate {
            override final type Left = L

            override final type Right = R
        }
    }
}