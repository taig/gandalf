package io.taig.android

import android.view.View
import io.taig.bsts._

package object bsts {
    implicit class ActionView[V <: View]( view: V ) extends operation.Action[V]( view )

    implicit class DataView[V <: View, T]( view: V )( implicit widget: Data[V, T] )
        extends operation.Data[V, T]( view )

    implicit class FeedbackView[V <: View: Feedback]( view: V ) extends operation.Feedback[V]( view )

    implicit class NameView[V <: View]( view: V ) extends operation.Name[V]( view )

    implicit class ValidatableView[V <: View, T]( view: V )( implicit description: Description[V, T] )
        extends operation.Validatable[V, T]( view )

    implicit class SearchView( view: View ) extends operation.Search( view )
}