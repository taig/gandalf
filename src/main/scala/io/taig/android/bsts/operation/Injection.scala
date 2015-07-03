package io.taig.android.bsts.operation

import android.view.View
import io.taig.android.bsts._

/**
 * Operations that enable a view to inject a value
 */
trait Injection[V <: View, T]
{
	def view: V

	/**
	 * Inject a value into this view (as described in its [[description.Injection]] type class)
	 */
	def value_=( value: T )( implicit i: description.Injection[V, T] ): Unit = i.inject( view, value )
}