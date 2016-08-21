package io.taig.gandalf.data

import io.taig.gandalf.Validation
import io.taig.gandalf.internal.Macro

import scala.language.experimental.macros
import scala.language.implicitConversions

trait Action {
    type Input

    type Output
}

object Action {
    type Input[I] = Action { type Input = I }

    type Output[O] = Action { type Output = O }

    type Aux[I, O] = Action { type Input = I; type Output = O }

    implicit def valueToLifted[I, A <: Action.Input[I]]( value: I )(
        implicit
        v: Validation[_, A]
    ): I Obeys A = macro Macro.liftInputAction[I, A]

    implicit def liftedToValue[A <: Action]( lifted: A#Input Obeys A ): A#Output = lifted.value
}