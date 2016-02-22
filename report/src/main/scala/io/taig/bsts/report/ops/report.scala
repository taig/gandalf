package io.taig.bsts.report.ops

import io.taig.bsts.report.Report

class report[I]( context: I ) {
    def report[O]( implicit r: Report.Aux[I, O] ): O = r.report( context )
}