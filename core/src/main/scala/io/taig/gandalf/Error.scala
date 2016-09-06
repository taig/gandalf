package io.taig.gandalf

import io.taig.gandalf.Rule.{ Applyable, Arguments }
import cats.data.NonEmptyList

import scala.reflect._

trait Error[-A <: Arguments] {
    def show( input: A#Arguments ): NonEmptyList[String]
}

object Error {
    @inline
    def apply[A <: Arguments]( implicit e: Error[A] ): Error[A] = e

    def instance[A <: Arguments]( f: A#Arguments ⇒ String ): Error[A] = {
        new Error[A] {
            override def show( input: A#Arguments ) = {
                NonEmptyList.of( f( input ) )
            }
        }
    }

    implicit def default[A <: Applyable: ClassTag]: Error[A] = instance { _ ⇒
        classTag[A].runtimeClass.getSimpleName.replace( "$", "" )
    }
}