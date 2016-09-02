package io.taig.gandalf

import cats.data._

import scala.reflect._

trait Error[-V <: Validatable] {
    def show( arguments: V#Arguments ): NonEmptyList[String]
}

object Error extends Error0 {
    implicit def errorOperator[O <: Operator]: Error[O] = new Error[O] {
        override def show( arguments: O#Arguments ) = arguments._2
    }
}

trait Error0 {
    /**
     * Summon a type class instance
     */
    @inline
    def apply[V <: Validatable]( implicit e: Error[V] ): Error[V] = e

    /**
     * Construct a type class instance
     */
    def instance[V <: Validatable]( f: V#Arguments ⇒ String ): Error[V] = {
        new Error[V] {
            override def show( values: V#Arguments ) = {
                NonEmptyList.of( f( values ) )
            }
        }
    }

    def identifier[V <: Validatable: ClassTag] = instance[V] { _ ⇒
        classTag[V].runtimeClass.getSimpleName.replace( "$", "" )
    }

    /**
     * Default error representation that simply renders the Actionable's name as
     * error message
     */
    implicit def errorValidatable[V <: Validatable: ClassTag] = identifier[V]
}