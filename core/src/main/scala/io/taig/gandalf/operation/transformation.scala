package io.taig.gandalf.operation

import io.taig.gandalf.data.{ Action, Transform, Transformation }
import io.taig.gandalf.syntax.aliases._

final class transformation[I, O, L <: Transformation.Aux[I, O]]( left: L ) {
    def ~>[P, R <: Action.Input[O]]( right: R with Action.Aux[O, P] ): L <~> R = new Transform[L, R]

    def <~>[P, R <: Action.Input[O]]( right: R with Action.Aux[O, P] ): L <~> R = new Transform[L, R]
}