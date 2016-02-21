package io.taig.bsts.android

import android.content.Context
import android.widget.TextView
import io.taig.bsts.android.implicits._
import io.taig.bsts.predef.all._

object Lab {
    val c: Context = ???
    val tv: TextView = ???

    val x = isDefined[String].validate( tv )
}