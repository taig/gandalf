package io.taig.gandalf.report.ops

import cats.data.{ NonEmptyList, Validated }
import io.taig.gandalf.Error
import io.taig.gandalf.report.{ Report, ReportableError }
import shapeless.HList

class reportError[N <: String, A <: HList]( error: Error[N, A] ) {
    def report[O]( implicit r: Report[Error[N, A], O] ): NonEmptyList[O] = r.report( error )
}

class reportReportableError[N <: String, A <: HList, O]( error: ReportableError[N, A, O] ) {
    def report: NonEmptyList[O] = error.r.report( error.e )
}

class reportValidated[E, A]( validated: Validated[E, A] ) {
    def report[O]( implicit r: Report[E, O] ): Validated[NonEmptyList[O], A] = validated.leftMap( r.report )
}