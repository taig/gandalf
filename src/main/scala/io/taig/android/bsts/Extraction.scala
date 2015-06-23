package io.taig.android.bsts

import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.EditText

/**
 * Describe how to extract a value that should be validated from the View
 */
trait	Extraction[V <: View, T]
extends	( V => T )

object Extraction
{
	implicit val editText = new Extraction[EditText, String]
	{
		override def apply( view: EditText ) = view.getText.toString
	}

	implicit val textInputLayout = new Extraction[TextInputLayout, String]
	{
		override def apply( view: TextInputLayout ) = view.getEditText.getText.toString
	}
}