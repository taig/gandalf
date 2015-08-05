package io.taig.android

import android.view.View

package object bsts
{
	implicit class	ActionView[V <: View, T]( val view: V )
	extends			operation.Action[V, T]

	implicit class	ExtractionView[V <: View, T]( val view: V )
	extends			operation.Extraction[V, T]

	implicit class	FeedbackView[V <: View]( val view: V )
	extends			operation.Feedback[V]

	implicit class	InjectionView[V <: View, T]( val view: V )
	extends			operation.Injection[V, T]

	implicit class	NameView[V <: View]( val view: V )
	extends			operation.Name[V]

	implicit class	SearchView( val view: View )
	extends			operation.Search

	implicit class	ValidationView[V <: View, T]( val view: V )
	extends			operation.Validation[V, T]
}