package io.taig.gandalf.ops

import io.taig.gandalf.operation.Mutate
import io.taig.gandalf.syntax.aliases._
import io.taig.gandalf.{ Action, Mutation }

final class mutation[I, O, L <: Mutation.Aux[I, O]]( left: L ) {
    def ~>[P, R <: Action.Input[O]]( right: R with Action.Aux[O, P] ): L <*> R = new Mutate[L, R]

    def <*>[P, R <: Action.Input[O]]( right: R with Action.Aux[O, P] ): L <*> R = new Mutate[L, R]
}