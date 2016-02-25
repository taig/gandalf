package io.taig.gandalf.predef

import io.taig.gandalf.Mutation

trait option {
    def isDefined[T] = Mutation[Option[T], T]( "isDefined" )( Predef.identity )
}

object option extends option