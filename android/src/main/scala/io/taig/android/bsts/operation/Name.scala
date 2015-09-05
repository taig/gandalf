package io.taig.android.bsts.operation

import android.view.View
import io.taig.android.bsts.R

/**
 * Attach a name to a view which is useful to match error messages to the view structure
 */
abstract class Name[V <: View]( view: V ) {
    def name( name: String ): V = {
        view.setTag( R.id.bsts_name, name )
        view
    }
}