package io.taig.android.bsts

import android.view.View

/**
 * Collection of all type classes that are necessary to make a view validatable
 */
trait Description[-V <: View, T]
        extends Injection[V, T]
        with Extraction[V, T]
        with Event[V]
        with Feedback[V] {
    override def onDetach( view: V ) = {}

    override def onAttach( view: V ) = {}
}

object Description {
    implicit def `Description`[V <: View: Event: Feedback, T]( implicit injection: Injection[V, T], extraction: Extraction[V, T] ) = new Description[V, T] {
        override def onAttach( view: V ) = implicitly[Event[V]].onAttach( view )

        override def onDetach( view: V ) = implicitly[Event[V]].onDetach( view )

        override def feedback( view: V, errors: Option[Seq[String]] ) = implicitly[Feedback[V]].feedback( view, errors )

        override def feedback( view: V ) = implicitly[Feedback[V]].feedback( view )

        override def inject( view: V, value: T ) = injection.inject( view, value )

        override def extract( view: V ) = extraction.extract( view )
    }
}