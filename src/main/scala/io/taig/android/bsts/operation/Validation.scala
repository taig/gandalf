package io.taig.android.bsts.operation

import android.view.View
import io.taig.android.bsts._
import io.taig.android.bsts.description.Descriptions

/**
 * Operations that enable a view to add validation rules for its content
 */
trait Validation[V <: View, T]
{
	def view: V

	def obeys( rules: Rule[T]* )( implicit h: description.Hooking[V, T], d: Descriptions[V, T] ): V =
	{
		h.attach( view, rules )
		view
	}

	/**
	 * Remove all validation rules from this view
	 */
	def reset()( implicit h: description.Hooking[V, T] ): Unit = h.reset( view )
}