package io.taig.gandalf.typelevel

import cats.data.Validated._

trait Required extends Rule {
    override type Input = String
}

object Required {
    implicit val evaluation = Evaluation.instance[Required] { input ⇒
        if ( input.nonEmpty ) {
            valid( input )
        } else {
            invalidNel( "required" )
        }
    }
}