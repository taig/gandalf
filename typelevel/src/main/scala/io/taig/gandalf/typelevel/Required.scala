package io.taig.gandalf.typelevel

import cats.data.Validated._

//@annotation.Rule[String]
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

    implicit val error = Error.instance[Required]( "required" )
}