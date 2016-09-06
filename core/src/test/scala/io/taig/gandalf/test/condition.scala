package io.taig.gandalf.test

import io.taig.gandalf.Rule.Arguments
import io.taig.gandalf._

object condition {
    object success
        extends Condition.With[String]( _ ⇒ true )
        with Arguments.None

    object failure
            extends Condition.With[String]( _ ⇒ false )
            with Arguments.None {
        implicit val error: Error[this.type] = Error.instance { _ ⇒
            "condition"
        }
    }
}