package io.taig.bsts.report.ops

import io.taig.bsts.report.Report

class report[T]( context: T ) {
    def report( implicit r: Report[T] ): r.Out = r.report( context )
}