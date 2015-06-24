package io.taig.android.bsts

import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.EditText

/**
 * Describe how to propagate error messages to the Ui
 */
trait	Feedback[V <: View]
extends	( ( V, Option[Seq[String]] ) => Unit )

object Feedback
{
	implicit val textInputLayout = new Feedback[TextInputLayout]
	{
		override def apply( view: TextInputLayout, errors: Option[Seq[String]] ) =
		{
			view.setError( errors.map( _.head ).orNull )
		}
	}

	implicit val editText = new Feedback[EditText]
	{
		override def apply( view: EditText, errors: Option[Seq[String]] ) = view.getParent match
		{
			case textInputLayout: TextInputLayout =>
			{
				implicitly[Feedback[TextInputLayout]].apply( textInputLayout, errors )
			}
			case _ => view.setError( errors.map( _.head ).orNull )
		}
	}
}