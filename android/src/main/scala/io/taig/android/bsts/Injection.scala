package io.taig.android.bsts

import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.AdapterView.INVALID_POSITION
import android.widget.{ AdapterView, CompoundButton, TextView }

trait Injection[-V <: View, -T] {
    def inject( view: V, value: T ): Unit
}

object Injection {
    implicit val `Injection[CompoundButton, Boolean]` = new Injection[CompoundButton, Boolean] {
        override def inject( view: CompoundButton, value: Boolean ) = view.setChecked( value )
    }

    implicit val `Injection[AdapterView, Int]` = new Injection[AdapterView[_], Int] {
        override def inject( view: AdapterView[_], value: Int ) = view.setSelection( value )
    }

    implicit val `Injection[AdapterView, Option[Int]]` = new Injection[AdapterView[_], Option[Int]] {
        override def inject( view: AdapterView[_], value: Option[Int] ) = {
            `Injection[AdapterView, Int]`.inject( view, value.getOrElse( INVALID_POSITION ) )
        }
    }

    implicit val `Injection[TextInputLayout, String]` = new Injection[TextInputLayout, String] {
        override def inject( view: TextInputLayout, value: String ) = {
            `Injection[TextView, String]`.inject( view.getEditText, value )
        }
    }

    implicit val `Injection[TextInputLayout, Option[String]]` = new Injection[TextInputLayout, Option[String]] {
        override def inject( view: TextInputLayout, value: Option[String] ) = {
            `Injection[TextView, Option[String]]`.inject( view.getEditText, value )
        }
    }

    implicit val `Injection[TextView, String]` = new Injection[TextView, String] {
        override def inject( view: TextView, value: String ) = view.setText( value )
    }

    implicit val `Injection[TextView, Option[String]]` = new Injection[TextView, Option[String]] {
        override def inject( view: TextView, value: Option[String] ) = {
            `Injection[TextView, String]`.inject( view, value.getOrElse( "" ) )
        }
    }
}