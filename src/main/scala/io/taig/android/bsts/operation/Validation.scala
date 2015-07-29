package io.taig.android.bsts.operation

import android.view.View
import io.taig.android.bsts._
import io.taig.android.bsts.description.Description
import io.taig.android.bsts.resource.R

/**
 * Operations that enable a view to add validation rules for its content
 */
trait Validation[V <: View, T]
{
	def view: V

	def obeys( rules: Rule[T]* )( implicit d: Description[V, T] ): V =
	{
		d.onAttach( view )
		view.setTag( R.id.validation_rules, Some( rules ) )
		view.setTag( R.id.validation_description, d )
		view
	}

	/**
	 * Remove all validation rules from this view
	 */
	def reset()( implicit d: Description[V, T] ): Unit =
	{
		d.onDetach( view )
		view.setTag( R.id.validation_rules, null )
		view.setTag( R.id.validation_description, null )
	}
}