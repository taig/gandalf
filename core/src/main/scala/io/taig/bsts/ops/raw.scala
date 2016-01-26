package io.taig.bsts.ops

import io.taig.bsts.Raw

final class raw[T]( context: T ) {
    def raw( implicit r: Raw[T] ): r.Out = r.raw( context )
}