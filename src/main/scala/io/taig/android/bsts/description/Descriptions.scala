package io.taig.android.bsts.description

import android.view.View

trait	Descriptions[V <: View, T]
extends	Extraction[V, T]
with	Feedback[V]

object Descriptions
{
	implicit def Descriptions[V <: View, T]( implicit h: Hooking[V, T], e: Extraction[V, T], f: Feedback[V] ): Descriptions[V, T] =
	{
		new Descriptions[V, T]
		{
			override def feedback( view: V, messages: Option[Seq[String]] ) = f.feedback( view, messages )

			override def extract( view: V ) = e.extract( view )
		}
	}
}