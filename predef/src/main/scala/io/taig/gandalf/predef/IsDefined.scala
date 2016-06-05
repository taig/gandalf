package io.taig.gandalf.predef

import io.taig.gandalf.{ Error, Evaluation, Mutation }
import shapeless._
import shapeless.syntax.singleton._

trait IsDefined[T] extends Mutation {
    override type Input = Option[T]

    override type Output = T

    override type Arguments = Error.Input[IsDefined[T]]
}

object IsDefined {
    implicit def evaluation[T]( implicit e: Error[IsDefined[T]] ) = {
        Evaluation.mutation[IsDefined[T]]( identity ) { input ⇒
            "input" ->> input :: HNil
        }
    }

    def isDefined[T]: IsDefined[T] = new IsDefined[T] {}
}