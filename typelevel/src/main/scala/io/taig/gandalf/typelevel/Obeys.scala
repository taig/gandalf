package io.taig.gandalf.typelevel

import scala.language.experimental.macros

case class Obeys[L, R <: Validation]( value: R#Output ) extends AnyVal with Serializable {
    override def toString = value.toString
}