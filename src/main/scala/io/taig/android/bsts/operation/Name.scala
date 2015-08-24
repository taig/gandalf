package io.taig.android.bsts.operation

import android.view.View
import io.taig.android.bsts.resource.R

/**
 * Attach a name to a view which is useful to match error messages to the view structure
 */
trait Name[V <: View] {
    def view: V

    def name( name: String ): V = {
        view.setTag( R.id.view_name, name )
        view
    }
}