package io.taig.android.bsts.rule.string

import android.content.Context
import io.taig.android.bsts.resource.R

case class	Max( value: Int )( implicit val context: Context )
extends		Length
{
	override def f = _ <= value

	override def message( value: String ) = context.getString( R.string.validation_string_max, value )
}