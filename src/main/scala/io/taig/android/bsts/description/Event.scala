package io.taig.android.bsts.description

import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.EditText
import io.taig.android.bsts._

trait Event[-V <: View]
{
	def onAttach( view: V ): Unit

	def onDetach( view: V ): Unit
}

object Event
{
	implicit val `Event[EditText]` = new Event[EditText] with View.OnFocusChangeListener
	{
		override def onAttach( view: EditText ) = view.setOnFocusChangeListener( this )

		override def onDetach( view: EditText ) = view.setOnFocusChangeListener( null )

		override def onFocusChange( view: View, hasFocus: Boolean ) = if( ! hasFocus ) view.validate()
	}

	implicit val `Event[TextInputLayout]` = new Event[TextInputLayout] with View.OnFocusChangeListener
	{
		override def onAttach( view: TextInputLayout ) = view.setOnFocusChangeListener( this )

		override def onDetach( view: TextInputLayout ) = view.setOnFocusChangeListener( null )

		override def onFocusChange( view: View, hasFocus: Boolean ) = if( ! hasFocus ) view.validate()
	}
}