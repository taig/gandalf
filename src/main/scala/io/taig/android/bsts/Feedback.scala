package io.taig.android.bsts

import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.EditText

/**
 * Describe how to propagate error messages to the Ui
 */
trait	Feedback[V <: View]
extends	( ( V, Seq[String] ) => Unit )

object Feedback
{
	implicit val textInputLayout = new Feedback[TextInputLayout]
	{
		override def apply( view: TextInputLayout, errors: Seq[String] ) = view.setError( errors.head )
	}

	implicit val editText = new Feedback[EditText]
	{
		override def apply( view: EditText, errors: Seq[String] ) = view.getParent match
		{
			case textInputLayout: TextInputLayout =>
			{
				implicitly[Feedback[TextInputLayout]].apply( textInputLayout, errors )
			}
			case _ => view.setError( errors.head )
		}
	}
}