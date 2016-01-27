package io.taig.bsts.android

import android.view.View
import android.widget.TextView

trait Injection[V <: View, -T] {
    def inject( view: V, value: T ): Unit
}

object Injection {
    def instance[V <: View, T]( f: ( V, T ) ⇒ Unit ): Injection[V, T] = new Injection[V, T] {
        override def inject( view: V, value: T ): Unit = f( view, value )
    }

    implicit val `Injection[TextView, CharSequence]`: Injection[TextView, CharSequence] = instance( _.setText( _ ) )

    implicit val `Injection[TextView, Option[CharSequence]]`: Injection[TextView, Option[CharSequence]] = {
        instance( ( view, value ) ⇒ view.setText( value.orNull ) )
    }
}