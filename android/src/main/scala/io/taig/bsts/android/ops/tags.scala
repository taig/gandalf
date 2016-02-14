package io.taig.bsts.android.ops

import android.view.View
import io.taig.bsts.android.Feedback
import io.taig.bsts.android.resource.R
import io.taig.bsts.data.{ Validated, NonEmptyList }

final private[android] class tags[V <: View]( view: V ) {
    def feedback_=( feedback: Feedback[V] ) = {
        view.setTag( R.id.bsts_feedback, feedback )
    }

    def feedback: Feedback[V] = {
        view.getTag( R.id.bsts_feedback ).asInstanceOf[Feedback[V]]
    }

    def name_=( name: String ) = {
        view.setTag( R.id.bsts_name, name )
    }

    def name: String = {
        view.getTag( R.id.bsts_name ).asInstanceOf[String]
    }

    def validation_=( validation: () ⇒ Validated[NonEmptyList[String], _] ) = {
        view.setTag( R.id.bsts_validation, validation )
    }

    def validation: () ⇒ Validated[NonEmptyList[String], _] = {
        view.getTag( R.id.bsts_validation ).asInstanceOf[() ⇒ Validated[NonEmptyList[String], _]]
    }
}
