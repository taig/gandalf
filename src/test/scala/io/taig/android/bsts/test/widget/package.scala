package io.taig.android.bsts.test

import android.widget.TextView

package object widget {
    implicit class ReflectiveTextInputLayout( textInputLayout: android.support.design.widget.TextInputLayout ) {
        def getErrorView: TextView = {
            val field = textInputLayout.getClass.getDeclaredField( "mErrorView" )
            field.setAccessible( true )
            field.get( textInputLayout ).asInstanceOf[TextView]
        }

        def getError: CharSequence = getErrorView.getText
    }
}