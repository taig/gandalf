package io.taig.bsts.android.ops

import android.view.View
import cats.data.Validated
import io.taig.android.viewvalue.{ Attribute, Extraction, Injection }
import io.taig.android.viewvalue.syntax.value._
import io.taig.bsts.Validation
import shapeless.HList

final class validation[I, O, V <: HList, E]( validation: Validation.Aux[I, O, V, E] ) {
    def validate[V <: View]( view: V )(
        implicit
        v: Extraction[Attribute.Value, V, I],
        e: Injection[Attribute.Error, V, Option[String]]
    ): Validated[E, O] = {
        val value = view.value[I]
        validation.validate( value )
    }
}