package io.taig.android.bsts.description

import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.EditText

/**
 * Describe how to extract a value that should be validated from the View
 */
trait Extraction[V <: View, T]
{
	def extract( view: V ): T
}

object Extraction
{
	implicit object	EditText
	extends			Extraction[EditText, String]
	{
		override def extract( view: EditText ) = view.getText.toString
	}

	implicit object	TextInputLayout
	extends			Extraction[TextInputLayout, String]
	{
		override def extract( view: TextInputLayout ) = implicitly[Extraction[EditText, String]].extract( view.getEditText )
	}
}