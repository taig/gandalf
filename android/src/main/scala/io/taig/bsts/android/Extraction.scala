package io.taig.bsts.android

import android.view.View
import android.widget.TextView

trait Extraction[V <: View, +T] {
    def extract( view: V ): T
}

object Extraction {
    def instance[V <: View, T]( f: V ⇒ T ): Extraction[V, T] = new Extraction[V, T] {
        override def extract( view: V ): T = f( view )
    }

    implicit val `Extraction[TextView, String]`: Extraction[TextView, String] = instance( _.getText.toString )

    implicit val `Extraction[TextView, Option[String]]`: Extraction[TextView, Option[String]] = instance {
        _.getText match {
            case text if text.length == 0 ⇒ None
            case text                     ⇒ Some( text.toString )
        }
    }
}