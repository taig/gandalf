package io.taig.android.bsts.description

import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.EditText
import io.taig.android.bsts._
import io.taig.android.bsts.resource.R

/**
 * Describe how validation rules should be attached to and retrieved from a View
 * 
 * There is usually no reason to change this behavior, it might be useful however to extend this class in order to
 * access the view's listeners.
 */
trait Hooking[V <: View, T]
{
	/**
	 * Attach rules to a view
	 */
	def attach( view: V, rules: Seq[Rule[T]] )( implicit description: Descriptions[V, T] ): Unit =
	{
		view.setTag( R.id.validation_rules, Some( rules ) )
		view.setTag( R.id.validation_description, description )
	}

	/**
	 * Remove rules from a view
	 */
	def reset( view: V ): Unit =
	{
		view.setTag( R.id.validation_rules, null )
		view.setTag( R.id.validation_description, null )
	}
}

object Hooking
{
	/**
	 * Attach an OnFocusChangeListener to the EditText that triggers validation when the focus leaves the widget
	 */
	implicit object	EditText
	extends			Hooking[EditText, String]
	with			View.OnFocusChangeListener
	{
		override def attach( view: EditText, rules: Seq[Rule[String]] )( implicit description: Descriptions[EditText, String] ) =
		{
			super.attach( view, rules )

			view.setOnFocusChangeListener( this )
		}

		override def reset( view: EditText ) =
		{
			super.reset( view )

			view.setOnFocusChangeListener( null )
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
	implicit object	TextInputLayout
	extends			Hooking[TextInputLayout, String]
	with			View.OnFocusChangeListener
	{
		override def attach( view: TextInputLayout, rules: Seq[Rule[String]] )( implicit description: Descriptions[TextInputLayout, String] ) =
		{
			super.attach( view, rules )

			view.setOnFocusChangeListener( this )
		}

		override def reset( view: TextInputLayout ) =
		{
			super.reset( view )

			view.setOnFocusChangeListener( null )
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