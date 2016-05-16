package io.taig.gandalf.typelevel

import shapeless.Witness
import cats.data.Validated._

trait Matches[T] extends Rule {
    override type Input = String
}

object Matches {
    implicit def evaluation[T <: String]( implicit w: Witness.Aux[T] ) = Evaluation.instance[Matches[T]] { input ⇒
        input == w.value match {
            case true  ⇒ valid( input )
            case false ⇒ invalidNel( "matches" )
        }
    }
}