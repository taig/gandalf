package io.taig.gandalf

import io.taig.gandalf.internal.TypeShow

import scala.reflect.ClassTag
import scala.reflect.classTag

trait Validation {
    type Input

    type Output
}

object Validation {
    type In[I] = Validation { type Input = I }

    type Out[O] = Validation { type Output = O }

    type Aux[I, O] = Validation { type Input = I; type Output = O }

    implicit def show[V <: Validation: ClassTag] = {
        TypeShow.instance[V]( s"${classTag[V].runtimeClass.getSimpleName}" )
    }
}