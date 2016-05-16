package io.taig.gandalf

import io.taig.gandalf.typelevel.Macro._

import scala.language.experimental.macros
import scala.language.implicitConversions

package object typelevel {
    implicit def valueToLifted[I <: V#Input, V <: Validation]( value: I )(
        implicit
        ev: Evaluation[V],
        er: Error[V]
    ): I Obeys V = macro lift_impl[I, V]

    def lift[V <: Validation]: LiftHelper[V] = new LiftHelper[V]

    class LiftHelper[V <: Validation] {
        def apply[I <: V#Input]( value: I )(
            implicit
            ev: Evaluation[V],
            er: Error[V]
        ): I Obeys V = macro lift_impl[I, V]
    }
}