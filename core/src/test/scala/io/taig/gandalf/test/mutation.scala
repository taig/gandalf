package io.taig.gandalf.test

import io.taig.gandalf._

object mutation {
    object success
        extends Mutation.With[String, String]( Some( _ ) )
        with Reportable.None

    object failure
            extends Mutation.With[String, String]( _ ⇒ None )
            with Reportable.None {
        implicit val error: Error[this.type] = {
            Error.instance( _ ⇒ "alteration" )
        }
    }
}