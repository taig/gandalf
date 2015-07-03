package io.taig.android.bsts.operation

import android.view.View
import io.taig.android.bsts._
import io.taig.android.bsts.description.Descriptions

/**
 * Operations that enable a view to have Rules attached and extracted
 */
private[bsts] trait Hooking[V <: View, T]
{
	def view: V

	/**
	 * Attach rules to this view (as described in its [[description.Hooking]] type class)
	 */
	def attach( rules: Seq[Rule[T]] )( implicit h: description.Hooking[V, T], d: Descriptions[V, T] ): Unit =
	{
		h.attach( view, rules )
	}

	/**
	 * Remove rules which are attached to this view (as described in its [[description.Hooking]] type class)
	 */
	def reset()( implicit h: description.Hooking[V, T] ): Unit = h.reset( view )
}