package io.taig.android.bsts.operation

import android.view.View
import io.taig.android.bsts.description

/**
 * Operations that enable a view to present validation feedback
 */
trait Feedback[V <: View]
{
	def view: V

	/**
	 * Show error messages in this view (or clear them with <code>None</code>)
	 */
	def indicate( values: Option[Seq[String]] )( implicit f: description.Feedback[V] ): Unit =
	{
		f.feedback( view, values )
	}

	/**
	 * Show error messages in this view
	 */
	def indicate( values: Seq[String] )( implicit f: description.Feedback[V] ): Unit = indicate( Some( values ) )

	/**
	 * Show an error message in this view
	 */
	def indicate( value: String )( implicit f: description.Feedback[V] ): Unit = indicate( Seq( value ) )
}