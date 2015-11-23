package io.taig.android.bsts

import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.{ RadioGroup, CompoundButton, AdapterView, EditText }

trait Event[V <: View] {
    def onAttach( view: V ): Unit

    def onDetach( view: V ): Unit
}

object Event {
    private def noop[V <: View] = new Event[V] {
        override def onAttach( view: V ) = {}

        override def onDetach( view: V ) = {}
    }

    implicit def `Event[AdapterView[_]]`[V <: AdapterView[_]] = noop[V]

    implicit def `Event[CompoundButton]`[V <: CompoundButton] = noop[V]

    implicit def `Event[RadioGroup]`[V <: RadioGroup] = noop[V]

    implicit def `Event[EditText]`[V <: EditText] = new Event[V] with View.OnFocusChangeListener {
        override def onAttach( view: V ) = view.setOnFocusChangeListener( this )

        override def onDetach( view: V ) = view.setOnFocusChangeListener( null )

        override def onFocusChange( view: View, hasFocus: Boolean ) = if ( !hasFocus ) view.validate()
    }

    implicit def `Event[TextInputLayout]`[V <: TextInputLayout] = new Event[V] with View.OnFocusChangeListener {
        override def onAttach( view: V ) = view.setOnFocusChangeListener( this )

        override def onDetach( view: V ) = view.setOnFocusChangeListener( null )

        override def onFocusChange( view: View, hasFocus: Boolean ) = if ( !hasFocus ) view.validate()
    }
}