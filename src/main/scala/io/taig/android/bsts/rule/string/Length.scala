package io.taig.android.bsts.rule.string

import io.taig.android.bsts.{ Rule, Transformation }

trait Length
        extends Rule[String]
        with Transformation[String] {
    def f: Int ⇒ Boolean

    override def transform( value: String ) = value.trim

    override protected def apply( value: String ) = value.length == 0 || f( value.length )
}