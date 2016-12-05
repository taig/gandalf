package io.taig.gandalf.predef

import io.taig.gandalf.core.{ Rule, Validation }

class trim extends Rule.Transition

object trim extends trim {
    implicit val string: Validation[trim, String, String] =
        Validation.transition( _.trim )

    class left extends Rule.Transition

    object left extends left {
        implicit val string: Validation[trim.left, String, String] =
            Validation.transition( _.replaceFirst( "^\\s*", "" ) )
    }

    class right extends Rule.Transition

    object right extends right {
        implicit val string: Validation[trim.right, String, String] =
            Validation.transition( _.replaceFirst( "\\s*$", "" ) )
    }
}