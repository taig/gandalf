package io.taig.android.bsts.operation

import io.taig.android.bsts.{ Feedback ⇒ TypeClass }
import android.view.View

abstract class Feedback[V <: View: TypeClass]( view: V ) {
    /**
     * Show error messages in this View (or clear them with <code>None</code>)
     */
    def indicate( errors: Option[Seq[String]] ): Unit = implicitly[TypeClass[V]].feedback( view, errors )

    /**
     * Show error messages in this view
     */
    def indicate( errors: String* ): Unit = indicate( Some( errors ) )

    /**
     * Retrieve the currently active error messages from this View
     */
    def indication: Option[Seq[String]] = implicitly[TypeClass[V]].feedback( view )
}