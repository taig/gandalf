package io.taig.android.bsts.operation

import android.view.View
import io.taig.android.bsts.{ Data ⇒ TypeClass }

abstract class Data[V <: View, T]( view: V )( implicit data: TypeClass[V, T] ) {
    def value: T = data.data( view )

    def value_=( value: T ): Unit = data.data( view, value )
}