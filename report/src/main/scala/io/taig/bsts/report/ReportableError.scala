package io.taig.bsts.report

import io.taig.bsts.Error
import shapeless.HList

case class ReportableError[N <: String, A <: HList](
        error: Error[N, A],
        r:     Report.Aux[Error[N, A], String]
) {
    def report = r.report( error )

    override def toString = error.toString
}