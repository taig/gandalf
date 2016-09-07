package io.taig.gandalf.core.test

import io.taig.gandalf.core._

object condition {
    object success
        extends Condition.With[String]( _ ⇒ true )
        with Reportable.None

    object failure
            extends Condition.With[String]( _ ⇒ false )
            with Reportable.None {
        implicit val error: Error[this.type] = {
            Error.one( _ ⇒ "condition" )
        }
    }
}