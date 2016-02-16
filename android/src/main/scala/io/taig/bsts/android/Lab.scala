package io.taig.bsts.android

import android.content.Context
import android.widget.TextView
import io.taig.bsts.data.Validated.{ Invalid, Valid }
import io.taig.bsts.ops.Computed
import io.taig.bsts.ops.hlist.NestedEvaluation
import io.taig.bsts.report.{ Report, ReportableError }
import io.taig.bsts.{ Error, Term }
import io.taig.bsts.android.implicits._
import io.taig.bsts.data.Validated
import io.taig.bsts.rule.all._
import shapeless.{ HNil, ::, HList }

import scala.Error

object Lab extends App {
    //    implicit val c: Context = ???
    //
    //    val tv: TextView = ???

    implicit val reportRequired = Report( max _ )( _ ⇒ "yolo" )

    //    println( ( required[String].as( "yolo" ) && min( 8 ) ).validate( "" ).report )
    println( ( max( 10 ) && min( 8 ).as( args ⇒ "minimi" ) ).validate( "" ).report )

    //    tv obeys required[String].as0( "yolo" ) && min( 8 )
}