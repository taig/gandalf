package io.taig.gandalf

import cats.data.NonEmptyList
import io.taig.gandalf.Rule.Applyable

import scala.reflect._

/**
 * Type class that describes how to show a failed validation
 */
trait Error[-R <: Reportable] {
    def show( input: R#Arguments ): NonEmptyList[String]
}

object Error {
    @inline
    def apply[R <: Reportable]( implicit e: Error[R] ): Error[R] = e

    def instance[R <: Reportable]( f: R#Arguments ⇒ String ): Error[R] = {
        new Error[R] {
            override def show( input: R#Arguments ) = {
                NonEmptyList.of( f( input ) )
            }
        }
    }

    implicit def default[A <: Applyable: ClassTag]: Error[A] = instance { _ ⇒
        classTag[A].runtimeClass.getSimpleName.replace( "$", "" )
    }
}