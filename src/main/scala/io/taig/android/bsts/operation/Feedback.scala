package io.taig.android.bsts.operation

import android.view.View
import io.taig.android.bsts._
import io.taig.android.bsts.resource.R

/**
 * Operations that enable a view to present validation feedback
 */
abstract class Feedback[V <: View]( implicit f: description.Feedback[V] ) {
    def view: V

    /**
     * Show error messages in this view (or clear them with <code>None</code>)
     */
    def indicate( errors: Option[Seq[String]] ): Unit = {
        f.feedback( view, errors )
    }

    /**
     * Show error messages in this view
     */
    def indicate( errors: Seq[String] ): Unit = indicate( Some( errors ) )

    /**
     * Show an error message in this view
     */
    def indicate( error: String ): Unit = indicate( Seq( error ) )
}