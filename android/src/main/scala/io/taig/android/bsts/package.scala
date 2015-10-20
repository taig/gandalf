package io.taig.android

import android.view.View
import io.taig.bsts._

package object bsts {
    implicit class ActionView[V <: View]( view: V ) extends operation.Action[V]( view )

    implicit class ExtractionView[V <: View]( view: V ) extends operation.Extraction( view )

    implicit class FeedbackView[V <: View: Feedback]( view: V ) extends operation.Feedback[V]( view )

    implicit class InjectionView[V <: View]( view: V ) extends operation.Injection( view )

    implicit class NameView[V <: View]( view: V ) extends operation.Name[V]( view )

    implicit class SearchView( view: View ) extends operation.Search( view )
}