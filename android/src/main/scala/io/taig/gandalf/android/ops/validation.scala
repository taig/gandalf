package io.taig.gandalf.android.ops

import android.view.View
import cats.data.Validated.{ Invalid, Valid }
import cats.data.{ NonEmptyList, Validated }
import io.taig.android.viewvalue.syntax.error._
import io.taig.android.viewvalue.syntax.value._
import io.taig.android.viewvalue.{ Extraction, Injection }
import io.taig.gandalf.Validation
import io.taig.gandalf.report.Report
import shapeless.HList

class validation[I, O, V <: HList, E]( validation: Validation.Aux[I, O, V, E] ) {
    def validateView[V <: View]( view: V )(
        implicit
        v: Extraction.Value[V, I],
        e: Injection.Error[V, Option[CharSequence]],
        r: Report[E, String]
    ): Validated[NonEmptyList[String], O] = {
        validation.validate( view.value[I] ).leftMap( r.report ) match {
            case valid @ Valid( _ ) ⇒
                view.error = None
                valid
            case invalid @ Invalid( NonEmptyList( error, _ ) ) ⇒
                view.error = Some( error )
                invalid
        }
    }
}