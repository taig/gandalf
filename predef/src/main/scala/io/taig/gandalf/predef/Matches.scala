package io.taig.gandalf.predef

import io.taig.gandalf.{ Error, Evaluation, Rule }
import shapeless.Witness

trait Matches[I, T <: I] extends Rule {
    override type Input = I
}

object Matches {
    implicit val error = Error.instance[Matches[_, _]]( "matches" )

    implicit def evaluation[I, T <: I]( implicit w: Witness.Aux[T], e: Error[Matches[I, T]] ) = {
        Evaluation.rule[Matches[I, T]]( _ == w.value )
    }

    def matches[I, T <: I]( w: Witness.Aux[T] )( implicit e: Error[Matches[I, T]] ): Matches[I, T] = {
        new Matches[I, T] {}
    }
}