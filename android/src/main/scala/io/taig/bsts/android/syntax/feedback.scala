package io.taig.bsts.android.syntax

import android.view.View
import io.taig.bsts.android.ops

import scala.language.implicitConversions

trait feedback {
    implicit def feedbackSyntax[V <: View]( view: V ): ops.feedback[V] = new ops.feedback[V]( view )
}

object feedback extends feedback