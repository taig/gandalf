package io.taig.gandalf.core

trait Serialization[C <: Container] {
    def serialize: String
}

object Serialization {
    def instance[C <: Container]( value: String ): Serialization[C] = {
        new Serialization[C] { override def serialize = value }
    }
}