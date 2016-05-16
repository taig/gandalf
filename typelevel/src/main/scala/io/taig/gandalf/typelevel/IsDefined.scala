package io.taig.gandalf.typelevel

import cats.data.Validated._

trait IsDefined[T] extends Mutation {
    override type Input = Option[T]

    override type Output = T
}

object IsDefined {
    implicit val error = Error.instance[IsDefined[_]]( "isDefined" )

    implicit def evaluation[T] = Evaluation.mutation[IsDefined[T]]( identity )
}