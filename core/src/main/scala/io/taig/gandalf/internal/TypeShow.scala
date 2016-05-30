package io.taig.gandalf.internal

import io.taig.gandalf.Validation

import scala.reflect._

trait TypeShow[T] {
    def show: String
}

object TypeShow {
    @inline
    def apply[T: TypeShow]: TypeShow[T] = implicitly[TypeShow[T]]

    def instance[T]( value: String ): TypeShow[T] = new TypeShow[T] {
        override def show = value
    }

    implicit def showValidation[V <: Validation: ClassTag] = {
        instance[V]( s"${classTag[V].runtimeClass.getSimpleName}" )
    }
}