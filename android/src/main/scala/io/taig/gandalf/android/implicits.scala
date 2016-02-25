package io.taig.gandalf.android

import io.taig.gandalf

trait implicits
    extends gandalf.implicits
    with gandalf.report.implicits
    with syntax.all
    with reports

object implicits extends implicits