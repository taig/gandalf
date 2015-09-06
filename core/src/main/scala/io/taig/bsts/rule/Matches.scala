package io.taig.bsts.rule

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

    implicit def `Definition[Matches]`[T] = Definition[Matches[T]]( ( value: T, matches: Matches[T] ) ⇒ {
        value == matches.value
    } )
}