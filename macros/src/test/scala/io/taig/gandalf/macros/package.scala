package io.taig.gandalf

package object macros {
    @obeys( condition.success )
    type Name = String

    case class ClassSuccess( @obeys( condition.success ) value:String )
    case class ClassFailure( @obeys( condition.failure ) value:String )
    case class ClassMultiple(
        @obeys( condition.success ) a:String,
        b:                           String,
        @obeys( condition.success ) c:String
    )
}