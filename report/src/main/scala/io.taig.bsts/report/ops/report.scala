package io.taig.bsts.report.ops

import io.taig.bsts.report.Report

final class report[T]( context: T ) {
    def report[O]( implicit r: Report.Aux[T, O] ): O = r.report( context )
}