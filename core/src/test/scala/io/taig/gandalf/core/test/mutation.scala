package io.taig.gandalf.core.test

import io.taig.gandalf.core._

object mutation {
    object success
        extends Mutation.With[String, String]( Some( _ ) )
        with Arguments.None

    object failure
            extends Mutation.With[String, String]( _ ⇒ None )
            with Arguments.None {
        implicit val error: Report[this.type] = {
            Report.static( "mutation" )
        }
    }
}