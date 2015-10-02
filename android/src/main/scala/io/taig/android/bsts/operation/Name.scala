package io.taig.android.bsts.operation

import android.view.View
import io.taig.android.bsts.resource.R
import io.taig.android.bsts

/**
 * Attach a name to a view which is useful to match error messages to the view structure
 */
abstract class Name[V <: View]( view: V ) {
    def name( name: String )( implicit feedback: bsts.Feedback[V] ): V = {
        view.setTag( R.id.bsts_name, name )
        view.setTag( R.id.bsts_feedback, feedback )
        view
    }
}