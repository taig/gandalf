package io.taig.gandalf.predef

import io.taig.gandalf._

class lowercase extends Rule.Transition

object lowercase extends lowercase {
    implicit val string: Validation[lowercase, String, String] =
        Validation.transition( _.toLowerCase )
}