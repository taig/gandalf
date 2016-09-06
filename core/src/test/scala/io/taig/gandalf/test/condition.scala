package io.taig.gandalf.test

import io.taig.gandalf._

object condition {
    object success
        extends Condition.With[String]( _ ⇒ true )
        with Reportable.None

    object failure
            extends Condition.With[String]( _ ⇒ false )
            with Reportable.None {
        implicit val error: Error[this.type] = Error.instance { _ ⇒
            "condition"
        }
    }
}