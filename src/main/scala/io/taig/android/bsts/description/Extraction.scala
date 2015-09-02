package io.taig.android.bsts.description

import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.TextView

/**
 * Describe how to extract a value that should be validated from the View
 */
trait Extraction[-V <: View, T] {
    def extract( view: V ): T
}

object Extraction {
    implicit val `Extraction[TextView, String]` = new Extraction[TextView, String] {
        override def extract( view: TextView ) = view.getText.toString
    }

    implicit val `Extraction[TextInputLayout, String]` = new Extraction[TextInputLayout, String] {
        override def extract( view: TextInputLayout ) = {
            implicitly[Extraction[TextView, String]].extract( view.getEditText )
        }
    }
}