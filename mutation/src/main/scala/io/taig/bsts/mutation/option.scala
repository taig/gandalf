package io.taig.bsts.mutation

trait option {
    def isDefined[T] = Mutation[Option[T], T]( "isDefined" )( Predef.identity )
}

object option extends option