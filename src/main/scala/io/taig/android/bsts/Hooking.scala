package io.taig.android.bsts

import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.EditText
import io.taig.android.bsts.resource.R

/**
 * Describe how validation rules, extraction and feedback should be attached to and extracted from a View
 * 
 * There is usually no reason to change this behavior, it might be useful however to extend this class in order to
 * access the view's listeners.
 */
trait Hooking[V <: View, T]
{
	def attach( view: V, rules: Seq[Rule[T]], extraction: Extraction[V, T], feedback: Feedback[V] ) =
	{
		view.setTag( R.id.validation_rules, rules )
		view.setTag( R.id.validation_extraction, extraction )
		view.setTag( R.id.validation_feedback, feedback )
	}

	def extract( view: V ): ( Seq[Rule[T]], Extraction[V, T], Feedback[V] ) = (
		view.getTag( R.id.validation_rules ).asInstanceOf[Seq[Rule[T]]],
		view.getTag( R.id.validation_extraction ).asInstanceOf[Extraction[V, T]],
		view.getTag( R.id.validation_feedback ).asInstanceOf[Feedback[V]]
	)
}

object Hooking
{
	/**
	 * Attach an OnFocusChangeListener to the EditText that triggers validation when the focus leaves the widget
	 */
	implicit val editText = new Hooking[EditText, String] with View.OnFocusChangeListener
	{
		override def attach( view: EditText, rules: Seq[Rule[String]], extraction: Extraction[EditText, String], feedback: Feedback[EditText] ) =
		{
			super.attach( view, rules, extraction, feedback )

			view.setOnFocusChangeListener( this )
		}

		override def onFocusChange( view: View, hasFocus: Boolean ) =
		{
			if( ! hasFocus )
			{
				view.validate()
			}
		}
	}

	/**
	 * Attach an OnFocusChangeListener to the EditText that triggers validation when the focus leaves the widget
	 */
	implicit val textInputLayout = new Hooking[TextInputLayout, String] with View.OnFocusChangeListener
	{
		override def attach( view: TextInputLayout, rules: Seq[Rule[String]], extraction: Extraction[TextInputLayout, String], feedback: Feedback[TextInputLayout] ) =
		{
			super.attach( view, rules, extraction, feedback )

			view.setOnFocusChangeListener( this )
		}

		override def onFocusChange( view: View, hasFocus: Boolean ) =
		{
			if( ! hasFocus )
			{
				view.validate()
			}
		}
	}
}