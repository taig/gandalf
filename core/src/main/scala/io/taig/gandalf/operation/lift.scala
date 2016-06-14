package io.taig.gandalf.operation

import io.taig.gandalf._
import io.taig.gandalf.data.{ Action, Obeys }
import io.taig.gandalf.internal.Macro
import io.taig.gandalf.syntax.aliases._

import scala.language.experimental.macros
import scala.language.implicitConversions

trait lift {
    implicit def valueToLifted[A <: Action]( value: A#Input )(
        implicit
        v: Validation[_, A]
    ): A#Input Obeys A = macro Macro.lift[A]

    implicit def liftedToValue[V <: Action]( lifted: V#Input Obeys V ): V#Output = lifted.value

    def lift[A <: Action]( value: A#Input )(
        implicit
        v: Validation[_, A]
    ): A#Input Obeys A = macro Macro.lift[A]

    def tryLift[A <: Action]( value: A#Input )( implicit v: Validation[_, A] ) = v.validate( value )
}