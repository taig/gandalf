package io.taig.gandalf.core

import scala.reflect.{ ClassTag, classTag }

trait Serialization[R <: Rule] {
    def serialize: String

    @inline
    override def toString = serialize
}

object Serialization {
    @inline
    def apply[R <: Rule]( implicit s: Serialization[R] ): Serialization[R] = s

    def instance[R <: Rule]( value: String ): Serialization[R] =
        new Serialization[R] { override def serialize = value }

    implicit def entity[E <: Rule.Entity: ClassTag]: Serialization[E] = {
        Serialization.instance {
            classTag[E].runtimeClass.getSimpleName.replace( "$", "" )
        }
    }
}