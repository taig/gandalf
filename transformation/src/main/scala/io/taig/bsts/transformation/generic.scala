package io.taig.bsts.transformation

trait generic {
    def identity[T] = Transformation[T, T]( "identity" )( Predef.identity )
}

object generic extends generic