package io.taig.gandalf.core

import cats.data.NonEmptyList
import cats.data.Validated._
import shapeless._

import scala.reflect._

trait Operator extends Rule {
    type Left <: Rule

    type Right <: Rule

    override type Input = Left#Input

    override type Output = Right#Output

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