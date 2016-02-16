package io.taig.bsts.android.ops

import android.view.View
import cats.data.{ NonEmptyList, Xor }
import io.taig.bsts.android.Feedback
import io.taig.bsts.android.resource.R

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

    def validation_=[O]( validation: () ⇒ Xor[NonEmptyList[String], O] ) = {
        view.setTag( R.id.bsts_validation, validation )
    }

    def validation[O]: () ⇒ Xor[NonEmptyList[String], O] = {
        view.getTag( R.id.bsts_validation ).asInstanceOf[() ⇒ Xor[NonEmptyList[String], O]]
    }
}
