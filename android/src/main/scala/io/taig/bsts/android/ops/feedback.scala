package io.taig.bsts.android.ops

import android.view.View
import io.taig.bsts.android.Feedback

final class feedback[V <: View]( view: V ) {
    def feedback( implicit f: Feedback[V] ): Option[String] = f.get( view )

    def feedback_=( error: CharSequence )( implicit f: Feedback[V] ): Unit = f.set( view, Option( error ) )

    def clear( implicit f: Feedback[V] ): Unit = f.set( view, None )
}