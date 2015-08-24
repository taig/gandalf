package io.taig.android.bsts.rule.string

import android.content.Context
import android.util.Patterns
import io.taig.android.bsts.resource.R
import io.taig.android.bsts.{ Rule, Transformation }

case class Min( value: Int )( implicit val context: Context )
        extends Length {
    override def f = _ >= value

    override def message( value: String ) = context.getString( R.string.validation_string_min ).format( this.value )
}