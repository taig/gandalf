package io.taig.gandalf.operation

import io.taig.gandalf.Action

case class Obeys[L, R <: Action]( value: R#Output ) extends AnyVal with Serializable {
    override def toString = value.toString
}