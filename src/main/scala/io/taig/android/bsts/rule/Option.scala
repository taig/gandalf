package io.taig.android.bsts.rule

import io.taig.android.bsts.Rule

trait Option[T]
        extends Rule[scala.Option[T]] {
    override protected def apply( value: scala.Option[T] ) = value.isDefined
}