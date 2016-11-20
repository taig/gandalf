package io.taig.gandalf.android.report

import android.content.Context
import io.taig.gandalf.core.Report
import io.taig.gandalf.android.R
import io.taig.gandalf.predef.string._

trait string {
    implicit def reportEmail(
        implicit
        c: Context
    ): Report[email.type] = {
        Report.static( c.getString( R.string.gandalf_string_email ) )
    }

    implicit def reportMatches(
        implicit
        c: Context
    ): Report[matches[_]] = {
        Report.static( c.getString( R.string.gandalf_string_matches ) )
    }

    implicit def reportRequired(
        implicit
        c: Context
    ): Report[required.type] = {
        Report.static( c.getString( R.string.gandalf_string_required ) )
    }
}

object string extends string