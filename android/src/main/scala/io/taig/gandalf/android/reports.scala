package io.taig.gandalf.android

import android.content.Context
import io.taig.gandalf.report.Report
import io.taig.gandalf.predef.all._
import io.taig.gandalf.{ Error, Term }
import shapeless.HList
import shapeless.record._

import scala.language.implicitConversions

trait reports {
    private def report[N <: String, A <: HList]( term: Term[N, _, _, A], message: Int )(
        implicit
        context: Context
    ): Report[Error[N, A], String] = Report( term )( _ ⇒ context.getString( message ) )

    implicit def reportEmail( implicit context: Context ) = report( email, R.string.gandalf_email )

    implicit def reportExactly( implicit context: Context ) = Report( exactly _ ) { arguments ⇒
        context.getString( R.string.gandalf_exactly ).format( arguments( "expected" ) )
    }

    implicit def reportMatches[T]( implicit context: Context ) = Report( matches[T] _ ) { _ ⇒
        context.getString( R.string.gandalf_matches )
    }

    implicit def reportMax( implicit context: Context ) = Report( max _ ) { arguments ⇒
        context.getString( R.string.gandalf_max ).format( arguments( "expected" ) )
    }

    implicit def reportMin( implicit context: Context ) = Report( min _ ) { arguments ⇒
        context.getString( R.string.gandalf_min ).format( arguments( "expected" ) )
    }

    implicit def reportPhone( implicit context: Context ) = report( phone, R.string.gandalf_phone )

    implicit def reportRequired( implicit context: Context ) = Report( required ) { _ ⇒
        context.getString( R.string.gandalf_required )
    }

    implicit def reportIsDefined( implicit context: Context ) = Report( isDefined ) { _ ⇒
        context.getString( R.string.gandalf_required )
    }
}

object reports extends reports