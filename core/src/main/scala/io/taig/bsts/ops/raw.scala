package io.taig.bsts.ops

import io.taig.bsts.Raw

class raw[I, O]( input: I ) {
    def raw( implicit r: Raw[I, O] ): O = r.raw( input )
}