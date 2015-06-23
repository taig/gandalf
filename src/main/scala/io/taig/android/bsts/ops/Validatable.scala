package io.taig.android.bsts.ops

import android.view.View
import io.taig.android.bsts._

/**
 * Allow attaching validation rules to a View
 */
trait Validatable[V <: View, T]
{
	def view: V

	implicit def hooking: Hooking[V, T]

	implicit def extraction: Extraction[V, T]

	implicit def feedback: Feedback[V]

	def withRule( rule: Rule[T] ): V = withRules( Seq( rule ) )

	def withRules( rules: Seq[Rule[T]] ): V =
	{
		view.setTag( R.id.validation_hooking, hooking.extract( _: V ) )
		hooking.attach( view, rules, extraction, feedback )
		view
	}
}