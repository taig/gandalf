package io.taig.android.bsts

import android.view.View
import io.taig.android.bsts.resource.R
import io.taig.bsts
import io.taig.bsts.Validation.combine
import io.taig.bsts.{ Result, Rule }
import shapeless.ops.hlist.LeftFolder
import shapeless.{ ::, HList, HNil }

class Validation[V <: View, T]( view: V )( implicit description: Description[V, T] ) {
    def obeys[S <: T, H <: HList]( validation: bsts.Validation[S, H] ): V = {
        description.onAttach( view )
        view.setTag( R.id.bsts_extraction, description )
        view.setTag( R.id.bsts_event, description )
        view.setTag( R.id.bsts_feedback, description )
        view.setTag( R.id.bsts_validation, validation )
        view
    }

    def obeys[S <: T, H <: HList]( rules: H )( implicit folder: LeftFolder.Aux[H, S, combine.type, Result[S]] ): V = {
        obeys( bsts.Validation[S, H]( rules ) )
    }

    def obeys[S <: T, R <: Rule]( rule: R )( implicit folder: LeftFolder.Aux[R :: HNil, S, combine.type, Result[S]] ): V = {
        obeys( rule :: HNil )
    }

    /**
     * Remove all validation rules from this view
     */
    def reset(): Unit = {
        view.getTag( R.id.bsts_event ).asInstanceOf[Event[V]].onDetach( view )

        view.setTag( R.id.bsts_extraction, null )
        view.setTag( R.id.bsts_event, null )
        view.setTag( R.id.bsts_feedback, null )
        view.setTag( R.id.bsts_validation, null )
    }
}

object Validation {
    def apply[T] = new Wrapper[T]

    class Wrapper[T] {
        def apply[V <: View]( view: V )( implicit description: Description[V, T] ) = new Validation[V, T]( view )
    }
}