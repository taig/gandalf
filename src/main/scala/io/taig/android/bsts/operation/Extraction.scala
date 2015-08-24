package io.taig.android.bsts.operation

import android.view.View

import io.taig.android.bsts._

/**
 * Operations that enable a view to extract its core value
 */
trait Extraction[V <: View, T] {
    def view: V

    /**
     * Retrieve the core value of this view (as described in its [[description.Extraction]] type class)
     */
    def value( implicit e: description.Extraction[V, T] ): T = e.extract( view )
}