package io.taig.bsts.report

import io.taig.bsts

trait implicits
    extends bsts.implicits
    with syntax.all

object implicits extends implicits