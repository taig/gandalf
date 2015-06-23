package io.taig.android.bsts.rule.string

import android.content.Context
import android.util.Patterns
import io.taig.android.bsts.{R, Rule, Transformation}

case class	Email( implicit context: Context )
extends		Rule[String]
with		Transformation[String]
{
	override def transform( value: String ) = value.trim

	override protected def apply( value: String ) =
	{
		value.length == 0 || Patterns.EMAIL_ADDRESS.matcher( value ).matches()
	}

	override def message( value: String ) = context.getString( R.string.validation_string_required )
}