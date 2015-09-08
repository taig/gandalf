package io.taig.android.bsts.operation

import android.view.View
import io.taig.android.bsts._
import io.taig.android.bsts.resource.R
import io.taig.bsts.Validation.combine
import io.taig.bsts._
import shapeless._
import shapeless.ops.hlist.LeftFolder

/**
 * Operations that enable a view to add validation rules for its content
 */
abstract class Validatable[V <: View, T]( view: V )( implicit description: Description[V, T] ) {
    def obeys[S <: T, H <: HList]( validation: Validation[S, H] ): V = {
        description.onAttach( view )
        view.setTag( R.id.bsts_validation, validation )
        view.setTag( R.id.bsts_description, description )
        view
    }

    def obeys[S <: T, H <: HList]( rules: H )( implicit folder: LeftFolder.Aux[H, S, combine.type, Result[S]] ): V = {
        obeys( Validation[S, H]( rules ) )
    }

    def obeys[S <: T, R <: Rule]( rule: R )( implicit folder: LeftFolder.Aux[R :: HNil, S, combine.type, Result[S]] ): V = {
        obeys( rule :: HNil )
    }

    /**
     * Remove all validation rules from this view
     */
    def reset()( implicit description: Description[V, T] ): Unit = {
        description.onDetach( view )
        view.setTag( R.id.bsts_validation, null )
        view.setTag( R.id.bsts_description, null )
    }
}