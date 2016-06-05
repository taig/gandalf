package io.taig.gandalf.ops

import io.taig.gandalf._
import io.taig.gandalf.internal.Macro
import io.taig.gandalf.operator.Obeys

import scala.language.experimental.macros
import scala.language.implicitConversions

trait lift {
    implicit def valueToLifted[I, A <: Action.In[I]]( value: I )(
        implicit
        i: Instance[A]
    ): I Obeys A = macro Macro.lift[I, A]

    implicit def liftedToValue[V <: Action]( lifted: V#Input Obeys V ): V#Output = lifted.value

    //    def lift[V <: Action]: LiftHelper[V] = new LiftHelper[V]
    //
    //    class LiftHelper[V <: Action] {
    //        def apply[I <: V#Input]( value: I )(
    //            implicit
    //            e:  V,
    //            ts: TypeShow[V]
    //        ): I Obeys V = macro Macro.lift[I, V]
    //    }

    //    def tryLift[V <: Action]( value: V#Input )(
    //        implicit
    //        e: V
    //    ): Result[V#Output] = e.validate( value )
}