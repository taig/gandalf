package io.taig.android.bsts

import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.AdapterView.INVALID_POSITION
import android.widget.{ RadioGroup, AdapterView, CompoundButton, TextView }

trait Injection[V <: View, -T] {
    def inject( view: V, value: T ): Unit
}

object Injection {
    implicit def `Injection[AdapterView, Int]`[V <: AdapterView[_]] = new Injection[V, Int] {
        override def inject( view: V, value: Int ) = view.setSelection( value )
    }

    implicit def `Injection[AdapterView, Option[Int]]`[V <: AdapterView[_]] = new Injection[V, Option[Int]] {
        override def inject( view: V, value: Option[Int] ) = {
            `Injection[AdapterView, Int]`.inject( view, value.getOrElse( INVALID_POSITION ) )
        }
    }

    implicit def `Injection[CompoundButton, Boolean]`[V <: CompoundButton] = new Injection[V, Boolean] {
        override def inject( view: V, value: Boolean ) = view.setChecked( value )
    }

    implicit def `Injection[RadioGroup, Int]`[V <: RadioGroup] = new Injection[V, Int] {
        override def inject( view: V, value: Int ) = view.check( value )
    }

    implicit def `Injection[RadioGroup, Option[Int]]`[V <: RadioGroup] = new Injection[V, Option[Int]] {
        override def inject( view: V, value: Option[Int] ) = value match {
            case Some( value ) ⇒ `Injection[RadioGroup, Int]`.inject( view, value )
            case None          ⇒ view.clearCheck()
        }
    }

    implicit def `Injection[TextInputLayout, String]`[V <: TextInputLayout] = new Injection[V, String] {
        override def inject( view: V, value: String ) = {
            `Injection[TextView, String]`.inject( view.getEditText, value )
        }
    }

    implicit def `Injection[TextInputLayout, Option[String]]`[V <: TextInputLayout] = new Injection[V, Option[String]] {
        override def inject( view: V, value: Option[String] ) = {
            `Injection[TextView, Option[String]]`.inject( view.getEditText, value )
        }
    }

    implicit def `Injection[TextView, String]`[V <: TextView] = new Injection[V, String] {
        override def inject( view: V, value: String ) = view.setText( value )
    }

    implicit def `Injection[TextView, Option[String]]`[V <: TextView] = new Injection[V, Option[String]] {
        override def inject( view: V, value: Option[String] ) = {
            `Injection[TextView, String]`.inject( view, value.getOrElse( "" ) )
        }
    }
}