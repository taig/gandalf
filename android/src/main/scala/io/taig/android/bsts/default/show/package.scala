package io.taig.android.bsts.default

import android.content.Context
import io.taig.android.bsts.R
import io.taig.bsts.rule._
import io.taig.bsts.{ Rule, Show }

package object show {
    private def show[R <: Rule]( id: Int, args: Any* )( implicit context: Context ) = {
        Show[R]( context.getString( id ).format( args: _* ) )
    }

    implicit def `Show[Email]`( implicit context: Context ) = show[Email]( R.string.bsts_email )

    implicit def `Show[Exactly]`[T]( implicit context: Context ) = Show[Exactly[T]]( ( value: T, rule: Exactly[T] ) ⇒ {
        context.getString( R.string.bsts_exactly ).format( rule.length )
    } )

    implicit def `Show[Max]`[T]( implicit context: Context ) = Show[Max[T]]( ( value: T, rule: Max[T] ) ⇒ {
        context.getString( R.string.bsts_max ).format( rule.length )
    } )

    implicit def `Show[Matches]`[T]( implicit context: Context ) = Show[Matches[T]]( ( value: T, rule: Matches[T] ) ⇒ {
        rule.target match {
            case Some( name ) ⇒ context.getString( R.string.bsts_matches_target ).format( name )
            case None         ⇒ context.getString( R.string.bsts_matches )
        }
    } )

    implicit def `Show[Min]`[T]( implicit context: Context ) = Show[Min[T]]( ( value: T, rule: Min[T] ) ⇒ {
        context.getString( R.string.bsts_min ).format( rule.length )
    } )

    implicit def `Show[Phone]`( implicit context: Context ) = show[Phone]( R.string.bsts_phone )

    implicit def `Show[Required]`[T]( implicit context: Context ) = show[Required[T]]( R.string.bsts_required )
}