package io.taig.bsts.android

import io.taig.bsts

trait implicits
    extends bsts.implicits
    with bsts.report.implicits
    with syntax.all
    with reports

object implicits extends implicits