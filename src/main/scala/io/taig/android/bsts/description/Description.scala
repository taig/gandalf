package io.taig.android.bsts.description

import android.view.View

trait	Description[-V <: View, T]
extends	Extraction[V, T]
with	Feedback[V]
with	Event[V]
{
	override def onAttach( view: V ) = {}

	override def onDetach( view: V ) = {}
}

object Description
{
	implicit def Descriptions[V <: View, T]( implicit ex: Extraction[V, T], f: Feedback[V], ev: Event[V] ): Description[V, T] =
	{
		new Description[V, T]
		{
			override def feedback( view: V, messages: Option[Seq[String]] ) = f.feedback( view, messages )

			override def extract( view: V ) = ex.extract( view )

			override def onAttach( view: V ) = ev.onAttach( view )

			override def onDetach( view: V ) = ev.onDetach( view )
		}
	}
}