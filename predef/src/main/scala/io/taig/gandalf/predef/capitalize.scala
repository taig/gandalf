package io.taig.gandalf.predef

import io.taig.gandalf.core.{ Rule, Validation }

class capitalize extends Rule.Transition

object capitalize extends capitalize {
    implicit val string: Validation[capitalize, String, String] =
        Validation.transition( _.capitalize )
}