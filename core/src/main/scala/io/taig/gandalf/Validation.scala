package io.taig.gandalf

import io.taig.gandalf.internal.TypeShow
import shapeless.HList

import scala.reflect.{ ClassTag, classTag }

trait Validation {
    type Input

    type Output

    type Arguments <: HList
}

object Validation {
    type In[I] = Validation { type Input = I }

    type Out[O] = Validation { type Output = O }

    type Aux[I, O] = Validation { type Input = I; type Output = O }

    implicit def show[V <: Validation: ClassTag] = {
        TypeShow.instance[V]( s"${classTag[V].runtimeClass.getSimpleName}" )
    }
}