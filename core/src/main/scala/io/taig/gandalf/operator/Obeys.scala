package io.taig.gandalf.operator

import io.taig.gandalf.Validation

case class Obeys[L, R <: Validation]( value: R#Output ) extends AnyVal with Serializable {
    override def toString = value.toString
}