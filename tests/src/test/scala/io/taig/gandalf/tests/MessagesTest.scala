package io.taig.gandalf.tests

import io.taig.gandalf.predef.{ Required, Trim }
import io.taig.gandalf.syntax.all._

class MessagesTest extends Suite {
    it should "be possible to provide custom messages for expressions" in {
        trait MyRequired extends ( Trim <~> Required )

        object MyRequired extends MyRequired

        MyRequired.validate( "yolo" )
    }
}