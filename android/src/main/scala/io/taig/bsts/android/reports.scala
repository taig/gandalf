package io.taig.bsts.android

import android.content.Context
import android.support.annotation.StringRes
import io.taig.bsts.android.resource.R
import io.taig.bsts.report.Report
import io.taig.bsts.predef.all._
import io.taig.bsts.{ Error, Term }
import shapeless.HList
import shapeless.record._

import scala.language.implicitConversions

trait reports {
    private def report[N <: String, A <: HList]( term: Term[N, _, _, A], @StringRes message: Int )(
        implicit
        context: Context
    ): Report.Aux[Error[N, A], String] = Report( term )( _ ⇒ context.getString( message ) )

    implicit def reportEmail( implicit context: Context ) = report( email, R.string.bsts_email )

    implicit def reportExactly( implicit context: Context ) = Report( exactly _ ) { arguments ⇒
        context.getString( R.string.bsts_exactly ).format( arguments( "expected" ) )
    }

    implicit def reportMatches[T]( implicit context: Context ) = Report( matches[T] _ ) { _ ⇒
        context.getString( R.string.bsts_matches )
    }

    implicit def reportMax( implicit context: Context ) = Report( max _ ) { arguments ⇒
        context.getString( R.string.bsts_max ).format( arguments( "expected" ) )
    }

    implicit def reportMin( implicit context: Context ) = Report( min _ ) { arguments ⇒
        context.getString( R.string.bsts_min ).format( arguments( "expected" ) )
    }

    implicit def reportPhone( implicit context: Context ) = report( phone, R.string.bsts_phone )

    implicit def reportRequired( implicit context: Context ) = Report( required ) { _ ⇒
        context.getString( R.string.bsts_required )
    }

    implicit def reportIsDefined( implicit context: Context ) = Report( isDefined ) { _ ⇒
        context.getString( R.string.bsts_required )
    }
}

object reports extends reports