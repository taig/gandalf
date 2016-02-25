package io.taig.gandalf.report

import io.taig.gandalf.Error
import shapeless.{ HList, Witness }

class ReportableError[N <: String, A <: HList, O]( val e: Error[N, A], val r: Report[Error[N, A], O] )(
    implicit
    w: Witness.Aux[N]
) extends Error[N, A]( e.arguments )

object ReportableError {
    implicit def reportReportableError[N <: String, A <: HList, O]: Report[ReportableError[N, A, O], O] = {
        new Report[ReportableError[N, A, O], O] {
            override def report( error: ReportableError[N, A, O] ) = error.r.report( error.e )
        }
    }
}