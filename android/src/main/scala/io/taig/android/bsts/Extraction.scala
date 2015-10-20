package io.taig.android.bsts

import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.{ AbsSpinner, CompoundButton, TextView }

trait Extraction[-V <: View, T] {
    def extract( view: V ): T
}

object Extraction {
    implicit val `Extraction[CompoundButton, Boolean]` = new Extraction[CompoundButton, Boolean] {
        override def extract( view: CompoundButton ) = view.isChecked
    }

    implicit val `Injection[Spinner, Int]` = new Extraction[AbsSpinner, Int] {
        override def extract( view: AbsSpinner ) = view.getSelectedItemPosition
    }

    implicit val `Injection[TextInputLayout, String]` = new Extraction[TextInputLayout, String] {
        override def extract( view: TextInputLayout ) = {
            `Extraction[TextView, String]`.extract( view.getEditText )
        }
    }

    implicit val `Extraction[TextView, String]` = new Extraction[TextView, String] {
        override def extract( view: TextView ) = view.getText.toString
    }
}