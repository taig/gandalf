package io.taig.gandalf.core

import cats.data.NonEmptyList
import cats.data.Validated._
import shapeless._

import scala.reflect._

trait Operator extends Rule with Container.Id {
    type Left <: Container

    type Right <: Container

    override type Input = Left#Kind#Input

    override type Output = Right#Kind#Output

    override final type Arguments = Input :: NonEmptyList[String] :: HNil
}

object Operator extends Operator0 {
    implicit def errorNot[O <: Operator: ClassTag](
        implicit
        e: Option[Error[O]]
    ): Error[not[O]] = Error.instance[not[O]] { arguments ⇒
        e.fold( arguments.at( 1 ) ) { error ⇒
            error.show( arguments ).map( error ⇒ s"not($error)" )
        }
    }

    implicit def errorSome[O <: Operator](
        implicit
        e: Error[O]
    ): Option[Error[O]] = Some( e )
}

trait Operator0 {
    implicit def errorNone[O <: Operator]: Option[Error[O]] = None
}