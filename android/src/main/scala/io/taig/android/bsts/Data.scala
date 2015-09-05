package io.taig.android.bsts

import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.{ Spinner, TextView }

/**
 * Type class that describes how to set or retrieve a value to/from a View
 */
trait Data[-V <: View, T] {
    def data( view: V ): T

    def data( view: V, value: T ): Unit
}

object Data {
    implicit val `Widget[Spinner, Int]` = new Data[Spinner, Int] {
        override def data( view: Spinner ) = view.getSelectedItemPosition

        override def data( view: Spinner, value: Int ) = view.setSelection( value )
    }

    implicit val `Widget[TextView, String]` = new Data[TextView, String] {
        override def data( view: TextView ) = view.getText.toString

        override def data( view: TextView, value: String ) = view.setText( value )
    }

    implicit val `Widget[TextInputLayout, String]` = new Data[TextInputLayout, String] {
        override def data( view: TextInputLayout ) = `Widget[TextView, String]`.data( view.getEditText )

        override def data( view: TextInputLayout, value: String ) = {
            `Widget[TextView, String]`.data( view.getEditText, value )
        }
    }
}