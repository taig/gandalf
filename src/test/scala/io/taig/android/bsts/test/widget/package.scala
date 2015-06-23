package io.taig.android.bsts.test

import android.widget.TextView

package object widget
{
	implicit class ReflectiveTextInputLayout( textInputLayout: android.support.design.widget.TextInputLayout )
	{
		def getError: CharSequence =
		{
			val field = textInputLayout.getClass.getDeclaredField( "mErrorView" )
			field.setAccessible( true )
			field.get( textInputLayout ).asInstanceOf[TextView].getText
		}
	}
}