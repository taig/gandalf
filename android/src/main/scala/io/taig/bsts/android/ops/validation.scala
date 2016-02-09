package io.taig.bsts.android.ops

import _root_.android.view.View
import io.taig.bsts.android.resources.R
import io.taig.bsts.android.{ Event, Extraction, Feedback }
import io.taig.bsts.ops.hlist.NestedEvaluation
import io.taig.bsts.report.Report
import io.taig.bsts.report.syntax.report._
import io.taig.bsts._
import shapeless.HList

final class validation[V <: View]( view: V ) {
    private[android] def feedback_=( feedback: Feedback[V] ) = {
        view.setTag( R.id.bsts_feedback, feedback )
    }

    private[android] def feedback: Feedback[V] = {
        view.getTag( R.id.bsts_feedback ).asInstanceOf[Feedback[V]]
    }

    private[android] def name_=( name: String ) = {
        view.setTag( R.id.bsts_name, name )
    }

    private[android] def name: String = {
        view.getTag( R.id.bsts_name ).asInstanceOf[String]
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
        def apply[O, W <: HList, R <: HList]( policy: Policy[I, O, W] )(
            implicit
            ev: Event[V],
            ex: Extraction[V, I],
            f:  Feedback[V],
            ne: NestedEvaluation.Aux[I, O, W, R],
            r:  Report.Aux[Failure[Computed[R], O], List[String]]
        ): V = {
            ev.onAttach( view )
            feedback = f
            validation = () ⇒ policy.validate( ex.extract( view ) ) match {
                case Success( _ )     ⇒ List.empty[String]
                case f @ Failure( _ ) ⇒ f.report
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