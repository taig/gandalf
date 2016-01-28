package io.taig.bsts.android

import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.TextView

trait Feedback[-V <: View] {
    def set( view: V, value: Option[CharSequence] ): Unit

    def get( view: V ): Option[String]
}

object Feedback {
    def instance[V <: View]( s: ( V, Option[CharSequence] ) ⇒ Unit, g: V ⇒ Option[String] ): Feedback[V] = new Feedback[V] {
        override def set( view: V, value: Option[CharSequence] ): Unit = s( view, value )

        override def get( view: V ): Option[String] = g( view )
    }

    implicit val `Feedback[TextView]`: Feedback[TextView] = instance(
        ( view, error ) ⇒ view.setError( error.orNull ),
        _.getError match {
            case error if error.length() == 0 ⇒ None
            case error                        ⇒ Some( error.toString )
        }
    )

    implicit val `Feedback[TextInputLayout]`: Feedback[TextInputLayout] = instance(
        ( view, error ) ⇒ view.setError( error.orNull ),
        _.getError match {
            case error if error.length() == 0 ⇒ None
            case error                        ⇒ Some( error.toString )
        }
    )
}