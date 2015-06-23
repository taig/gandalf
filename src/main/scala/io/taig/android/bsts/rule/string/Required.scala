package io.taig.android.bsts.rule.string

import android.content.Context
import io.taig.android.bsts.{R, Rule, Transformation}

case class	Required()( implicit context: Context )
extends		Rule[String]
with		Transformation[String]
{
	override def transform( value: String ) = value.trim

	override protected def apply( value: String ) = value.length > 0

	override def message( value: String ) = context.getString( R.string.validation_string_required )
}