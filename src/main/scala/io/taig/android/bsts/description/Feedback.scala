package io.taig.android.bsts.description

import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.EditText

/**
 * Describe how to show or remove error messages in a View
 */
trait Feedback[V <: View]
{
	def feedback( view: V, messages: Option[Seq[String]] ): Unit
}

object Feedback
{
	implicit val `Feedback[TextInputLayout]` = new Feedback[TextInputLayout]
	{
		override def feedback( view: TextInputLayout, errors: Option[Seq[String]] ) =
		{
			view.setError( errors.flatMap( _.headOption ).orNull )
		}
	}

	implicit val `Feedback[EditText]` = new Feedback[EditText]
	{
		override def feedback( view: EditText, errors: Option[Seq[String]] ) = view.getParent match
		{
			case textInputLayout: TextInputLayout =>
			{
				implicitly[Feedback[TextInputLayout]].feedback( textInputLayout, errors )
			}
			case _ => view.setError( errors.flatMap( _.headOption ).orNull )
		}
	}
}