package io.taig.bsts.android.ops

import android.view.View
import cats.data.Validated.{ Invalid, Valid }
import cats.data.{ NonEmptyList, OneAnd, Validated }
import io.taig.android.viewvalue.syntax.error._
import io.taig.android.viewvalue.syntax.value._
import io.taig.android.viewvalue.{ Extraction, Injection }
import io.taig.bsts.Validation
import io.taig.bsts.report.Report
import shapeless.HList

class validation[I, O, V <: HList, E]( validation: Validation.Aux[I, O, V, E] ) {
    def validateView[V <: View]( view: V )(
        implicit
        v: Extraction.Value[V, I],
        e: Injection.Error[V, Option[CharSequence]],
        r: Report.Aux[Validated[E, O], Validated[NonEmptyList[String], O]]
    ): Validated[NonEmptyList[String], O] = r.report( validation.validate( view.value[I] ) ) match {
        case valid @ Valid( _ ) ⇒
            view.error = None
            valid
        case invalid @ Invalid( OneAnd( error, _ ) ) ⇒
            view.error = Some( error )
            invalid
    }
}