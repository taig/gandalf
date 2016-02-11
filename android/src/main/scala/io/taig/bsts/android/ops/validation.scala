package io.taig.bsts.android.ops

import android.view.View
import io.taig.bsts.android.resources.R
import io.taig.bsts.android.{ Extraction, Event, Feedback }
import io.taig.bsts._
import io.taig.bsts.android.syntax.validation._
import io.taig.bsts.report.Report

import scala.language.experimental.macros

final class validation[V <: View]( view: V ) {
    private[android] def feedback_=( feedback: Feedback[V] ) = {
        view.setTag( R.id.bsts_feedback, feedback )
    }

    private[android] def feedback: Feedback[V] = {
        view.getTag( R.id.bsts_feedback ).asInstanceOf[Feedback[V]]
    }

    private[android] def validation_=( validation: () ⇒ List[String] ) = {
        view.setTag( R.id.bsts_validation, validation )
    }

    private[android] def validation: () ⇒ List[String] = {
        view.getTag( R.id.bsts_validation ).asInstanceOf[() ⇒ List[String]]
    }

    /**
     * Attach a policy to this view
     */
    def obeys[I]: Builder1[I] = new Builder1[I]

    class Builder1[I] {
        def apply[O]( validation: Validation[I, O] )(
            implicit
            ev: Event[V],
            ex: Extraction[V, I],
            f:  Feedback[V],
            r:  Report.Aux[Failure[validation.F, O], List[String]]
        ): V = {
            ev.onAttach( view )
            view.feedback = f
            view.validation = () ⇒ validation.validate( ex.extract( view ) ) match {
                case Success( _ )         ⇒ List.empty[String]
                case f @ Failure( error ) ⇒ r.report( f )
            }
            view
        }
    }

    /**
     * Remove all validation rules from this view
     */
    def reset()( implicit ev: Event[V] ): Unit = {
        ev.onDetach( view )
        feedback = null
        validation = null
    }
}