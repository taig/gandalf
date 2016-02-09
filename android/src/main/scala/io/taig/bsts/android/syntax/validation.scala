package io.taig.bsts.android.syntax

import android.view.View
import io.taig.bsts.android.ops

import scala.language.implicitConversions

trait validation {
    implicit def validationSyntax[V <: View]( view: V ): ops.validation[V] = new ops.validation[V]( view )
}

object validation extends validation