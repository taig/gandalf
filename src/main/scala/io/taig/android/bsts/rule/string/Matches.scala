package io.taig.android.bsts.rule.string

import android.content.Context
import io.taig.android.bsts.{Transformation, rule}

case class	Matches( value: () => String, target: Option[String] = None )( implicit val context: Context )
extends		rule.Matches[String]
with		Transformation[String]
{
	override def transform( value: String ) = value.trim
}