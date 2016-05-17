package io.taig.gandalf.typelevel

import shapeless.Witness

trait Regex[T <: String] extends Rule {
    override type Input = String
}

object Regex {
    implicit val error = Error.instance[Regex[_]]( "regex" )

    implicit def evaluation[T <: String]( implicit w: Witness.Aux[T], e: Error[Regex[T]] ) = {
        Evaluation.rule[Regex[T]]( _ matches w.value )
    }

    def regex[T <: String]( regex: Witness.Aux[T] )( implicit e: Error[Regex[T]] ): Evaluation[Regex[T]] = {
        evaluation[T]( regex, e )
    }
}