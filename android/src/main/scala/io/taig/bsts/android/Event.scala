package io.taig.bsts.android

import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.{ TextView, EditText }

trait Event[-V <: View] {
    def onAttach( view: V ): Unit

    def onDetach( view: V ): Unit
}

object Event extends Event0 {
    implicit val `Event[TextView]` = new Event[TextView] with View.OnFocusChangeListener {
        override def onAttach( view: TextView ) = view.setOnFocusChangeListener( this )

        override def onDetach( view: TextView ) = view.setOnFocusChangeListener( null )

        override def onFocusChange( view: View, hasFocus: Boolean ) = ??? // if ( !hasFocus ) view.validate()
    }

    implicit val `Event[TextInputLayout]` = new Event[TextInputLayout] with View.OnFocusChangeListener {
        override def onAttach( view: TextInputLayout ) = view.setOnFocusChangeListener( this )

        override def onDetach( view: TextInputLayout ) = view.setOnFocusChangeListener( null )

        override def onFocusChange( view: View, hasFocus: Boolean ) = ??? //if ( !hasFocus ) view.validate()
    }
}

trait Event0 {
    implicit val `Event[View]` = new Event[View] {
        override def onAttach( view: View ) = {}

        override def onDetach( view: View ) = {}
    }
}