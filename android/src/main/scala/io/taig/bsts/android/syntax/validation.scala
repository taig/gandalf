package io.taig.bsts.android.syntax

import android.view.View
import io.taig.bsts.android.ops

import scala.language.implicitConversions

trait validation {
    implicit def validationSyntax[V <: View]( view: V ): ops.validation[V] = new ops.validation[V]( view )

    implicit def viewSyntax[V <: View]( view: V ): ops.view[V] = new ops.view[V]( view )
}

object validation extends validation