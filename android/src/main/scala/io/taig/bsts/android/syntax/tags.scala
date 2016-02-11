package io.taig.bsts.android.syntax

import android.view.View
import io.taig.bsts.android.ops

import scala.language.implicitConversions

private[android] trait tags {
    implicit def viewTagsSyntax[V <: View]( view: V ): ops.tags[V] = new ops.tags( view )
}

private[android] object tags extends tags