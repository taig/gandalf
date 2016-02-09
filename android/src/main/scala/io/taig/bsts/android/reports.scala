package io.taig.bsts.android

import android.content.Context
import android.support.annotation.StringRes
import io.taig.bsts.{ Rule, Error }
import io.taig.bsts.android.resources.R
import io.taig.bsts.report.Report
import io.taig.bsts.rules.all._
import shapeless.HList
import shapeless.record._

import scala.language.implicitConversions

object reports {
    private def report[N <: String, A <: HList]( rule: Rule[N, _, A], @StringRes message: Int )(
        implicit
        context: Context
    ): Report.Aux[Error[N, A], String] = Report( rule )( context.getString( message ) )

    implicit def `Report[email]`( implicit context: Context ) = report( email, R.string.bsts_email )

    implicit def `Report[exactly]`( implicit context: Context ) = Report( exactly _ ) as { arguments ⇒
        context.getString( R.string.bsts_exactly ).format( arguments( "expected" ) )
    }

    implicit def `Report[matches]`( implicit context: Context ) = Report( matches _ ) as { _ ⇒
        context.getString( R.string.bsts_matches )
    }

    implicit def `Report[max]`( implicit context: Context ) = Report( max _ ) as { arguments ⇒
        context.getString( R.string.bsts_max ).format( arguments( "expected" ) )
    }

    implicit def `Report[min]`( implicit context: Context ) = Report( min _ ) as { arguments ⇒
        context.getString( R.string.bsts_min ).format( arguments( "expected" ) )
    }

    implicit def `Report[phone]`( implicit context: Context ) = report( phone, R.string.bsts_phone )

    implicit def `Report[required]`( implicit context: Context ) = report( required, R.string.bsts_required )
}