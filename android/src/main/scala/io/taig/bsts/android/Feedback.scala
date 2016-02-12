package io.taig.bsts.android

import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.TextView

trait Feedback[-V <: View] {
    def set( view: V, error: Option[CharSequence] ): Unit

    def get( view: V ): Option[String]

    def contramap[W <: View]( f: W ⇒ V ): Feedback[W] = Feedback.instance(
        ( view, error ) ⇒ set( f( view ), error ),
        view ⇒ get( f( view ) )
    )
}

object Feedback {
    def apply[V <: View: Feedback]: Feedback[V] = implicitly[Feedback[V]]

    def instance[V <: View]( s: ( V, Option[CharSequence] ) ⇒ Unit, g: V ⇒ Option[String] ): Feedback[V] = new Feedback[V] {
        override def set( view: V, error: Option[CharSequence] ): Unit = s( view, error )

        override def get( view: V ): Option[String] = g( view )
    }

    implicit val `Feedback[TextInputLayout]`: Feedback[TextInputLayout] = instance(
        ( view, error ) ⇒ view.setError( error.orNull ),
        _.getError match {
            case error if error.length() == 0 ⇒ None
            case error                        ⇒ Some( error.toString )
        }
    )

    implicit val `Feedback[TextView]`: Feedback[TextView] = instance(
        ( view, error ) ⇒ view.getParent match {
            case view: TextInputLayout ⇒ Feedback[TextInputLayout].set( view, error )
            case _                     ⇒ view.setError( error.orNull )
        },
        view ⇒ view.getParent match {
            case view: TextInputLayout ⇒ Feedback[TextInputLayout].get( view )
            case _ ⇒ view.getError match {
                case error if error.length() == 0 ⇒ None
                case error                        ⇒ Some( error.toString )
            }
        }
    )
}