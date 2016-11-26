package io.taig.gandalf.predef

import io.taig.gandalf.core.{ Rule, Validation }

class trim extends Rule.Transition

object trim extends trim {
    implicit val string: Validation[trim, String, String] =
        Validation.transition( _.trim )
}