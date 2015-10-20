package io.taig.android.bsts

import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.{ AdapterView, AbsSpinner, CompoundButton, TextView }

trait Extraction[-V <: View, T] {
    def extract( view: V ): T
}

object Extraction {
    implicit val `Extraction[CompoundButton, Boolean]` = new Extraction[CompoundButton, Boolean] {
        override def extract( view: CompoundButton ) = view.isChecked
    }

    implicit val `Extraction[AdapterView, Int]` = new Extraction[AdapterView[_], Int] {
        override def extract( view: AdapterView[_] ) = view.getSelectedItemPosition
    }

    implicit val `Extraction[Spinner, Option[Int]]` = new Extraction[AbsSpinner, Option[Int]] {
        override def extract( view: AbsSpinner ) = `Extraction[AdapterView, Int]`.extract( view ) match {
            case AdapterView.INVALID_POSITION ⇒ None
            case position                     ⇒ Some( position )
        }
    }

    implicit val `Extraction[TextInputLayout, String]` = new Extraction[TextInputLayout, String] {
        override def extract( view: TextInputLayout ) = {
            `Extraction[TextView, String]`.extract( view.getEditText )
        }
    }

    implicit val `Extraction[TextInputLayout, Option[String]]` = new Extraction[TextInputLayout, Option[String]] {
        override def extract( view: TextInputLayout ) = {
            `Extraction[TextView, Option[String]]`.extract( view.getEditText )
        }
    }

    implicit val `Extraction[TextView, String]` = new Extraction[TextView, String] {
        override def extract( view: TextView ) = view.getText.toString
    }

    implicit val `Extraction[TextView, Option[String]]`: Extraction[TextView, Option[String]] = new Extraction[TextView, Option[String]] {
        override def extract( view: TextView ) = {
            Some( `Extraction[TextView, String]`.extract( view ) ).filter( _.trim.nonEmpty )
        }
    }
}