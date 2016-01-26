package io.taig.bsts

trait Raw[T] {
    type Out

    def raw( context: T ): Out
}