package io.taig.bsts.android.ops

import android.view.View
import io.taig.bsts.android.{ Injection, Extraction }

final class value[V <: View]( view: V ) {
    def value: Builder = new Builder

    class Builder {
        def apply[T]( implicit e: Extraction[V, T] ) = e.extract( view )
    }

    def value_=[T]( value: T )( implicit i: Injection[V, T] ): Unit = i.inject( view, value )
}