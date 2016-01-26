package io.taig.bsts

sealed trait Validation[E, S] {
    def isSuccess: Boolean

    def isFailure: Boolean = !isSuccess

    def toEither: Either[E, S]
}

case class Success[E, S]( val value: S ) extends Validation[E, S] {
    override val isSuccess = true

    override def toEither: Either[E, S] = Right( value )

    override def toString = s"Success($value)"
}

case class Failure[E, S]( val value: E ) extends Validation[E, S] {
    override val isSuccess = false

    override def toEither: Either[E, S] = Left( value )

    override def toString = s"Failure($value)"
}