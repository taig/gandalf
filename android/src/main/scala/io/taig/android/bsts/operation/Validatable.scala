package io.taig.android.bsts.operation

import android.view.View
import io.taig.android.bsts._
import io.taig.android.bsts.resource.R
import io.taig.bsts._

/**
 * Operations that enable a view to add validation rules for its content
 */
abstract class Validatable[V <: View, T]( view: V )( implicit description: Description[V, T] ) {
    def obeys( rules: Runtime[_ <: Rule.Aux[T]]* ): V = {
        description.onAttach( view )
        view.setTag( R.id.bsts_rules, rules )
        view.setTag( R.id.bsts_description, description )
        view
    }

    /**
     * Remove all validation rules from this view
     */
    def reset()( implicit description: Description[V, T] ): Unit = {
        description.onDetach( view )
        view.setTag( R.id.bsts_rules, null )
        view.setTag( R.id.bsts_description, null )
    }
}