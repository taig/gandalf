package io.taig.bsts.android

import android.content.Context
import io.taig.bsts.android.resources.R
import io.taig.bsts.report.Report
import io.taig.bsts.rules.all._

import scala.language.implicitConversions

object reports {
    implicit def `Report[required]`( implicit context: Context ) = {
        Report( required )( context.getString( R.string.bsts_required ) )
    }
}