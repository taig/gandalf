package io.taig.bsts.report

import io.taig.bsts.Error
import shapeless.{ HList, Witness }

class ReportableError[N <: String, A <: HList, O]( val e: Error[N, A], val r: Report[Error[N, A], O] )(
    implicit
    w: Witness.Aux[N]
) extends Error[N, A]( e.arguments )