package io.taig.gandalf.core

import scala.reflect.{ ClassTag, classTag }

trait Serialization[R] {
    def serialize: String
}

object Serialization {
    @inline
    def apply[R]( implicit s: Serialization[R] ): Serialization[R] = s

    def instance[R]( value: String ): Serialization[R] = {
        new Serialization[R] { override def serialize = value }
    }

    implicit def serialization[R: ClassTag]: Serialization[R] = {
        Serialization.instance {
            classTag[R].runtimeClass.getSimpleName.replace( "$", "" )
        }
    }
}