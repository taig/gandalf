package io.taig.gandalf.core

trait Serialization[R <: Rule] {
    def serialize: String
}

object Serialization {
    def instance[R <: Rule]( value: String ): Serialization[R] = {
        new Serialization[R] {
            override def serialize = value
        }
    }
}