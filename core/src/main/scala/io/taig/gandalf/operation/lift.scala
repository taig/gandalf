package io.taig.gandalf.operation

import io.taig.gandalf._
import io.taig.gandalf.data.{ Action, Obeys }
import io.taig.gandalf.internal.Macro

import scala.language.experimental.macros
import scala.language.implicitConversions

trait lift {
    implicit def valueToLifted[I, A <: Action.Input[I]]( value: I )(
        implicit
        v: Validation[_, A]
    ): I Obeys A = macro Macro.liftInputAction[I, A]

    implicit def liftedToValue[A <: Action]( lifted: A#Input Obeys A ): A#Output = lifted.value

    def lift[A <: Action]( value: A#Input )(
        implicit
        v: Validation[_, A]
    ): A#Input Obeys A = macro Macro.liftAction[A]

    def tryLift[A <: Action]( value: A#Input )( implicit v: Validation[_, A] ) = v.validate( value )
}