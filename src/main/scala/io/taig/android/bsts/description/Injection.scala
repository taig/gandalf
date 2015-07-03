package io.taig.android.bsts.description

import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.EditText

/**
 * Describe how to inject a value into a View
 */
trait Injection[V <: View, T]
{
	def inject( view: V, value: T ): Unit
}

object Injection
{
	implicit object	EditText
	extends			Injection[EditText, String]
	{
		override def inject( view: EditText, value: String ) = view.setText( value )
	}

	implicit object	TextInputLayout
	extends			Injection[TextInputLayout, String]
	{
		override def inject( view: TextInputLayout, value: String ) =
		{
			implicitly[Injection[EditText, String]].inject( view.getEditText, value )
		}
	}
}