package io.taig.gandalf.core

import cats.data.NonEmptyList
import io.taig.gandalf.core.Rule.Applyable

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

    def instance[R <: Reportable]( f: R#Arguments ⇒ NonEmptyList[String] ): Error[R] = {
        new Error[R] {
            override def show( input: R#Arguments ) = f( input )
        }
    }

    def one[R <: Reportable]( f: R#Arguments ⇒ String ): Error[R] = {
        instance( arguments ⇒ NonEmptyList.of( f( arguments ) ) )
    }

    implicit def default[A <: Applyable: ClassTag]: Error[A] = one { _ ⇒
        classTag[A].runtimeClass.getSimpleName.replace( "$", "" )
    }
}