package io.taig.android.bsts

import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.EditText

/**
 * Type class that describes how to set or retrieve errors to/from a View
 */
trait Feedback[-V <: View] {
    def feedback( view: V, errors: Option[Seq[String]] ): Unit

    def feedback( view: V ): Option[Seq[String]]
}

object Feedback {
    implicit val `Feedback[TextInputLayout]` = new Feedback[TextInputLayout] {
        override def feedback( view: TextInputLayout, errors: Option[Seq[String]] ) = {
            view.setError( errors.flatMap( _.headOption ).orNull )
        }

        override def feedback( view: TextInputLayout ) = {
            Option( view.getError ).map( errors ⇒ Seq( errors.toString ) )
        }
    }

    implicit val `Feedback[EditText]` = new Feedback[EditText] {
        override def feedback( view: EditText, errors: Option[Seq[String]] ) = {
            view.getParent match {
                case textInputLayout: TextInputLayout ⇒
                    implicitly[Feedback[TextInputLayout]].feedback( textInputLayout, errors )
                case _ ⇒
                    view.setError( errors.flatMap( _.headOption ).orNull )
            }
        }

        override def feedback( view: EditText ) = {
            view.getParent match {
                case textInputLayout: TextInputLayout ⇒
                    implicitly[Feedback[TextInputLayout]].feedback( textInputLayout )
                case _ ⇒ Option( view.getError ).map( errors ⇒ Seq( errors.toString ) )
            }
        }
    }
}