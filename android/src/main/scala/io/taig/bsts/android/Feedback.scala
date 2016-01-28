package io.taig.bsts.android

import android.view.View
import android.widget.TextView

trait Feedback[V <: View] {
    def set( view: V, value: Option[String] ): Unit

    def get( view: V ): Option[String]
}

object Feedback {
    implicit val `Feedback[TextView]`: Feedback[TextView] = new Feedback[TextView] {
        override def set( view: TextView, value: Option[String] ): Unit = view.setError( value.orNull )

        override def get( view: TextView ): Option[String] = view.getError match {
            case error if error.length() == 0 ⇒ None
            case error                        ⇒ Some( error.toString )
        }
    }
}