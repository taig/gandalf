package io.taig.gandalf.predef

import io.taig.gandalf.{ Error, Evaluation, Mutation }

trait IsDefined[T] extends Mutation {
    override type Input = Option[T]

    override type Output = T
}

object IsDefined {
    implicit val error = Error.instance[IsDefined[_]]( "isDefined" )

    implicit def evaluation[T] = Evaluation.mutation[IsDefined[T]]( identity )

    def isDefined[T]: IsDefined[T] = new IsDefined[T] {}
}