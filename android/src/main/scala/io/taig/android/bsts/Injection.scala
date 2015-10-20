package io.taig.android.bsts

import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.{ AbsSpinner, CompoundButton, TextView }

trait Injection[-V <: View, -T] {
    def inject( view: V, value: T ): Unit
}

object Injection {
    implicit val `Injection[CompoundButton, Boolean]` = new Injection[CompoundButton, Boolean] {
        override def inject( view: CompoundButton, value: Boolean ) = view.setChecked( value )
    }

    implicit val `Injection[Spinner, Int]` = new Injection[AbsSpinner, Int] {
        override def inject( view: AbsSpinner, value: Int ) = view.setSelection( value )
    }

    implicit val `Injection[TextInputLayout, String]` = new Injection[TextInputLayout, String] {
        override def inject( view: TextInputLayout, value: String ) = {
            `Injection[TextView, String]`.inject( view.getEditText, value )
        }
    }

    implicit val `Injection[TextView, String]` = new Injection[TextView, String] {
        override def inject( view: TextView, value: String ) = view.setText( value )
    }
}