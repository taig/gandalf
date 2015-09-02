package io.taig.android.bsts.description

import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.TextView

/**
 * Describe how to inject a value into a View
 */
trait Injection[-V <: View, T] {
    def inject( view: V, value: T ): Unit
}

object Injection {
    implicit val `Injection[TextView, String]` = new Injection[TextView, String] {
        override def inject( view: TextView, value: String ) = view.setText( value )
    }

    implicit val `Injection[TextInputLayout, String]` = new Injection[TextInputLayout, String] {
        override def inject( view: TextInputLayout, value: String ) = {
            implicitly[Injection[TextView, String]].inject( view.getEditText, value )
        }
    }
}