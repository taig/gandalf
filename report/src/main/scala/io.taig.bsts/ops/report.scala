package io.taig.bsts.ops

import io.taig.bsts._

final class report[T]( context: T ) {
    def report( implicit r: Report[T] ): r.Out = r.report( context )
}