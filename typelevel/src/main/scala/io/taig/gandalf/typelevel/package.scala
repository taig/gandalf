package io.taig.gandalf

import cats.data.Validated
import io.taig.gandalf.typelevel.Macro._

import scala.language.experimental.macros
import scala.language.implicitConversions

package object typelevel {
    type ~>[L <: Mutation, R <: Validation.In[L#Output]] = Apply[L, R]

    type &[L <: Rule, R <: Rule.Aux[L#Input]] = EagerAnd[L, R]

    type &&[L <: Rule, R <: Rule.Aux[L#Input]] = LazyAnd[L, R]

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

    def tryLift[V <: Validation]( value: V#Input )(
        implicit
        ev: Evaluation[V],
        er: Error[V]
    ): Validated[List[String], V#Output] = ev.validate( value )
}