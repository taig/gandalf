package io.taig.android.bsts

import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.{ RadioGroup, CompoundButton, AdapterView, EditText }

trait Event[-V <: View] {
    def onAttach( view: V ): Unit

    def onDetach( view: V ): Unit
}

object Event {
    private def noop[V <: View] = new Event[V] {
        override def onAttach( view: V ) = {}

        override def onDetach( view: V ) = {}
    }

    implicit val `Event[AdapterView[_]]` = noop[AdapterView[_]]

    implicit val `Event[CompoundButton]` = noop[CompoundButton]

    implicit val `Event[RadioGroup]` = noop[RadioGroup]

    implicit val `Event[EditText]` = new Event[EditText] with View.OnFocusChangeListener {
        override def onAttach( view: EditText ) = view.setOnFocusChangeListener( this )

        override def onDetach( view: EditText ) = view.setOnFocusChangeListener( null )

        override def onFocusChange( view: View, hasFocus: Boolean ) = if ( !hasFocus ) view.validate()
    }

    implicit val `Event[TextInputLayout]` = new Event[TextInputLayout] with View.OnFocusChangeListener {
        override def onAttach( view: TextInputLayout ) = view.setOnFocusChangeListener( this )

        override def onDetach( view: TextInputLayout ) = view.setOnFocusChangeListener( null )

        override def onFocusChange( view: View, hasFocus: Boolean ) = if ( !hasFocus ) view.validate()
    }
}