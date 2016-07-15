package io.taig

import io.taig.gandalf.data.{ Action, Obeys }
import io.taig.gandalf.internal.Macro

import scala.language.experimental.macros

package object gandalf {
    def lift[A <: Action]( value: A#Input )(
        implicit
        v: Validation[_, A]
    ): A#Input Obeys A = macro Macro.liftAction[A]

    def lift[A <: Action]( action: A )( value: A#Input )(
        implicit
        v: Validation[_, A]
    ): A#Input Obeys A = macro Macro.liftActionRuntime[A]

    def tryLift[A <: Action]( value: A#Input )( implicit v: Validation[_, A] ) = v.validate( value )

    def tryLift[A <: Action]( action: A )( value: A#Input )( implicit v: Validation[_, A] ) = v.validate( value )
}