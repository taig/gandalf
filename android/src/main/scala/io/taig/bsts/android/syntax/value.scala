package io.taig.bsts.android.syntax

import android.view.View
import io.taig.bsts.android.ops

import scala.language.implicitConversions

trait value {
    implicit def valueSyntax[V <: View]( view: V ): ops.value[V] = new ops.value[V]( view )
}

object value extends value