package io.taig.gandalf.data

case class Obeys[L, R <: Action]( value: R#Output ) extends AnyVal with Serializable {
    override def toString = value.toString
}