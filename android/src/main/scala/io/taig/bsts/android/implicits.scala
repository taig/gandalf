package io.taig.bsts.android

import io.taig.bsts

trait implicits
    extends bsts.implicits
    with syntax.all

object implicits extends implicits