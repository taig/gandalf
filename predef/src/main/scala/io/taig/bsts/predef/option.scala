package io.taig.bsts.predef

import io.taig.bsts.Mutation

trait option {
    def isDefined[T] = Mutation[Option[T], T]( "isDefined" )( Predef.identity )
}

object option extends option