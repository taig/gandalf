package io.taig.android.bsts

import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.AdapterView.INVALID_POSITION
import android.widget._

trait Extraction[-V <: View, T] {
    def extract( view: V ): T
}

object Extraction {
    implicit val `Extraction[AdapterView, Int]` = new Extraction[AdapterView[_], Int] {
        override def extract( view: AdapterView[_] ) = view.getSelectedItemPosition
    }

    implicit val `Extraction[AdapterView, Option[Int]]` = new Extraction[AdapterView[_], Option[Int]] {
        override def extract( view: AdapterView[_] ) = `Extraction[AdapterView, Int]`.extract( view ) match {
            case INVALID_POSITION ⇒ None
            case position         ⇒ Some( position )
        }
    }

    implicit val `Extraction[CompoundButton, Boolean]` = new Extraction[CompoundButton, Boolean] {
        override def extract( view: CompoundButton ) = view.isChecked
    }

    implicit val `Extraction[RadioGroup, Int]` = new Extraction[RadioGroup, Int] {
        override def extract( view: RadioGroup ) = view.getCheckedRadioButtonId
    }

    implicit val `Extraction[RadioGroup, Option[Int]]` = new Extraction[RadioGroup, Option[Int]] {
        override def extract( view: RadioGroup ) = `Extraction[RadioGroup, Int]`.extract( view ) match {
            case -1 ⇒ None
            case id ⇒ Some( id )
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