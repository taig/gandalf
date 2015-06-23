package io.taig.android

import android.view.View

package object bsts
{
	implicit class	ValidatableView[V <: View, T]( val view: V )(
		implicit val extraction: Extraction[V, T],
		val feedback: Feedback[V],
		val hooking: Hooking[V, T] = new Hooking[V, T] {}
	)
	extends			ops.Validatable[V, T]

	implicit class	ValidationView[V <: View, T]( val view: V )
	extends			ops.Validation[V, T]
}