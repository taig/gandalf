package io.taig.bsts.android

import android.content.Context
import android.widget.TextView
import io.taig.bsts.{ Failure, Success, Policy }
import io.taig.bsts.android.syntax.all._
import io.taig.bsts.report.syntax.report._
import io.taig.bsts.android.reports._
import io.taig.bsts.rules.string._
import io.taig.bsts.rules.generic.required

object Lab {
    val asdf: TextView = ???
    implicit val c: Context = ???

    val r = min( 10 ).validate( "foobar" ) match {
        case Success( _ )     ⇒ ???
        case f @ Failure( _ ) ⇒ f.report
    }

    asdf obeys[String] min( 10 )
    asdf obeys[String] required
}