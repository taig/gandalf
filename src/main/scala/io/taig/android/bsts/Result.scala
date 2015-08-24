package io.taig.android.bsts

sealed trait Result[T] {
    def isSuccess: Boolean

    def isFailure: Boolean = !isSuccess

    def value: T
}

/**
 * Successful validation
 *
 * @param value Value that passed the validation
 */
case class Success[T]( value: T )
        extends Result[T] {
    override val isSuccess = true
}

/**
 * Failed Validation
 *
 * @param value Value that caused validation to fail
 */
case class Failure[T]( value: T, errors: Seq[String] )
        extends Result[T] {
    override val isSuccess = false
}