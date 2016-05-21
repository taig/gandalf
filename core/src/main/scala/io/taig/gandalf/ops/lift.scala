package io.taig.gandalf.ops

import cats.data.Validated
import io.taig.gandalf.internal.{ Macro, TypeShow }
import io.taig.gandalf.operator.Obeys
import io.taig.gandalf.{ Error, Evaluation, Validation }

import scala.language.experimental.macros
import scala.language.implicitConversions

trait lift {
    implicit def valueToLifted[I <: V#Input, V <: Validation]( value: I )(
        implicit
        ev: Evaluation[V],
        er: Error[V],
        ts: TypeShow[V]
    ): I Obeys V = macro Macro.lift[I, V]

    implicit def liftedToValue[V <: Validation]( lifted: V#Input Obeys V ): V#Output = lifted.value

    def lift[V <: Validation]: LiftHelper[V] = new LiftHelper[V]

    class LiftHelper[V <: Validation] {
        def apply[I <: V#Input]( value: I )(
            implicit
            ev: Evaluation[V],
            er: Error[V],
            ts: TypeShow[V]
        ): I Obeys V = macro Macro.lift[I, V]
    }

    def tryLift[V <: Validation]( value: V#Input )(
        implicit
        ev: Evaluation[V],
        er: Error[V]
    ): Validated[List[String], V#Output] = ev.validate( value )
}