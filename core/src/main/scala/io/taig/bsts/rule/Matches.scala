package io.taig.bsts.rule

import algebra.Eq
import io.taig.bsts.{ Definition, Rule }

trait Matches[T] extends Rule {
    override type Value = T

    def value: T

    def target: Option[String]
}

object Matches {
    def apply[T]( comparison: ⇒ T, name: Option[String] = None ): Matches[T] = new Matches[T] {
        override def value = comparison

        override def target = name
    }

    def value[T]( comparison: T, name: Option[String] = None ): Matches[T] = new Matches[T] {
        override val value = comparison

        override def target = name
    }

    implicit def `Definition[Matches]`[T: Eq] = Definition[Matches[T]]( ( value: T, matches: Matches[T] ) ⇒ {
        implicitly[Eq[T]].eqv( value, matches.value )
    } )
}