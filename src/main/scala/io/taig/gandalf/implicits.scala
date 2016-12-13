package io.taig.gandalf

trait implicits
    extends syntax.all
    with macros.implicits
    with report.implicits

object implicits extends implicits