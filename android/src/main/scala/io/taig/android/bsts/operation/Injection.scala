package io.taig.android.bsts.operation

import android.view.View
import io.taig.android.bsts.{ Injection ⇒ TypeClass }

abstract class Injection[V <: View]( view: V ) {
    def set[T]( value: T )( implicit injection: TypeClass[V, T] ) = injection.inject( view, value )
}