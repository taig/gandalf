package io.taig.bsts

sealed trait Result[+T] {
    def value: T

    def isSuccess: Boolean

    def isFailure: Boolean = !isSuccess
}

case class Success[+T]( value: T ) extends Result[T] {
    override val isSuccess = true
}

case class Failure[+T]( value: T, errors: Seq[String] ) extends Result[T] {
    override val isSuccess = false
}