package io.taig.gandalf.circe.test

import io.taig.gandalf.macros.obeys
import io.taig.gandalf.predef.string.required

case class User( @obeys( required ) name:String )