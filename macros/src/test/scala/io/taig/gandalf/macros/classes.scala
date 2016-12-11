package io.taig.gandalf.macros

import io.taig.gandalf.condition

case class ClassSuccess( @obeys( condition.success ) value:String )
case class ClassFailure( @obeys( condition.failure ) value:String )