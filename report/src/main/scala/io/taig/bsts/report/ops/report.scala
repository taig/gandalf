package io.taig.bsts.report.ops

import io.taig.bsts.report.Report

final class report[T]( context: T ) {
    def report[E]( implicit r: Report.Aux0[T, E] ): r.Out = r.report( context )
}