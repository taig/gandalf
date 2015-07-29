package io.taig.android.bsts.operation

import android.view.View

import io.taig.android.bsts.description

trait Event[V <: View]
{
	def view: V

	def onAttach()( implicit e: description.Event[V] ) = e.onAttach( view )

	def onDetach()( implicit e: description.Event[V] ) = e.onDetach( view )
}