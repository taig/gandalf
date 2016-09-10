package io.taig.gandalf.predef

package object numeric {
    def zero[T]( implicit n: Numeric[T] ): T = n.zero
}