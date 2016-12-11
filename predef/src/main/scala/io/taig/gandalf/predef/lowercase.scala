package io.taig.gandalf.predef

import io.taig.gandalf.core.{ Rule, Validation }

class lowercase extends Rule.Transition

object lowercase extends lowercase {
    implicit val string: Validation.Aux[lowercase, String, String] =
        Validation.transition( _.toLowerCase )
}