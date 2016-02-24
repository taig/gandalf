package io.taig.bsts.android

import io.taig.bsts
import io.taig.android.viewvalue

trait implicits
    extends bsts.implicits
    with bsts.report.implicits
    with syntax.all
    with reports

object implicits extends implicits