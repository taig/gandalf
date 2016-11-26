package io.taig.gandalf.predef

import io.taig.gandalf.core.{ &&, not }

object required extends ( trim && not[empty] )