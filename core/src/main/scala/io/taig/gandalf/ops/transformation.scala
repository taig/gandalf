package io.taig.gandalf.ops

import io.taig.gandalf.operation.Transform
import io.taig.gandalf.syntax.aliases._
import io.taig.gandalf.{ Action, Transformation }

final class transformation[I, O, L <: Transformation.Aux[I, O]]( left: L ) {
    def ~>[P, R <: Action.Input[O]]( right: R with Action.Aux[O, P] ): L <~> R = new Transform[L, R]

    def <~>[P, R <: Action.Input[O]]( right: R with Action.Aux[O, P] ): L <~> R = new Transform[L, R]
}