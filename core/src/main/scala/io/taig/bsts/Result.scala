package io.taig.bsts

sealed trait Result[+E, +S] extends Product with Serializable {
    def isSuccess: Boolean

    def isFailure: Boolean = !isSuccess

    def toEither: Either[E, S]
}

final case class Success[+E, +S]( val value: S ) extends Result[E, S] {
    override val isSuccess = true

    override def toEither: Either[E, S] = Right( value )

    override def toString = s"Success($value)"
}

final case class Failure[+E, +S]( val value: E ) extends Result[E, S] {
    override val isSuccess = false

    override def toEither: Either[E, S] = Left( value )

    override def toString = s"Failure($value)"
}