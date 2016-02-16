package io.taig.bsts.report

import io.taig.bsts

trait implicits
    extends bsts.implicits
    with ops.nestedEvaluation
    with syntax.all

object implicits extends implicits