package io.taig.gandalf.core

import scala.reflect.{ ClassTag, classTag }

trait Serialization[R <: Rule] {
    def serialize: String
}

object Serialization {
    @inline
    def apply[R <: Rule]( implicit s: Serialization[R] ): Serialization[R] = s

    def instance[R <: Rule]( value: String ): Serialization[R] = {
        new Serialization[R] { override def serialize = value }
    }

    implicit def serialization[R <: Rule: ClassTag]: Serialization[R] = {
        Serialization.instance {
            classTag[R].runtimeClass.getSimpleName.replace( "$", "" )
        }
    }
}