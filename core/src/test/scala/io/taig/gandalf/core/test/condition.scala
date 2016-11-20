package io.taig.gandalf.core.test

import io.taig.gandalf.core._

object condition {
    object success
        extends Condition.With[String]( _ ⇒ true )
        with Arguments.None

    object failure
            extends Condition.With[String]( _ ⇒ false )
            with Arguments.None {
        implicit val error: Report[this.type] = {
            Report.static( "condition" )
        }
    }
}