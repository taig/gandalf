package io.taig.android.bsts.operation

import android.view.View
import io.taig.android.bsts.{ Extraction ⇒ TypeClass }

abstract class Extraction[V <: View]( view: V ) {
    def get[T]()( implicit extraction: TypeClass[V, T] ) = extraction.extract( view )
}