package io.taig.gandalf

case class Obeys[L, R <: Validation]( value: R#Output ) extends AnyVal with Serializable {
    override def toString = value.toString
}
