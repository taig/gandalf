package io.taig.android.bsts

import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.AdapterView.INVALID_POSITION
import android.widget._

trait Extraction[V <: View, T] {
    def extract( view: V ): T
}

object Extraction {
    implicit def `Extraction[AdapterView, Int]`[V <: AdapterView[_]] = new Extraction[V, Int] {
        override def extract( view: V ) = view.getSelectedItemPosition
    }

    implicit def `Extraction[AdapterView, Option[Int]]`[V <: AdapterView[_]] = new Extraction[V, Option[Int]] {
        override def extract( view: V ) = `Extraction[AdapterView, Int]`.extract( view ) match {
            case INVALID_POSITION ⇒ None
            case position         ⇒ Some( position )
        }
    }

    implicit def `Extraction[CompoundButton, Boolean]`[V <: CompoundButton] = new Extraction[V, Boolean] {
        override def extract( view: V ) = view.isChecked
    }

    implicit def `Extraction[RadioGroup, Int]`[V <: RadioGroup] = new Extraction[V, Int] {
        override def extract( view: V ) = view.getCheckedRadioButtonId
    }

    implicit def `Extraction[RadioGroup, Option[Int]]`[V <: RadioGroup] = new Extraction[V, Option[Int]] {
        override def extract( view: V ) = `Extraction[RadioGroup, Int]`.extract( view ) match {
            case -1 ⇒ None
            case id ⇒ Some( id )
        }
    }

    implicit def `Extraction[TextInputLayout, String]`[V <: TextInputLayout] = new Extraction[V, String] {
        override def extract( view: V ) = {
            `Extraction[TextView, String]`.extract( view.getEditText )
        }
    }

    implicit def `Extraction[TextInputLayout, Option[String]]`[V <: TextInputLayout] = new Extraction[V, Option[String]] {
        override def extract( view: V ) = {
            `Extraction[TextView, Option[String]]`.extract( view.getEditText )
        }
    }

    implicit def `Extraction[TextView, String]`[V <: TextView] = new Extraction[V, String] {
        override def extract( view: V ) = view.getText.toString
    }

    implicit def `Extraction[TextView, Option[String]]`[V <: TextView]: Extraction[V, Option[String]] = new Extraction[V, Option[String]] {
        override def extract( view: V ) = {
            Some( `Extraction[TextView, String]`.extract( view ) ).filter( _.trim.nonEmpty )
        }
    }
}