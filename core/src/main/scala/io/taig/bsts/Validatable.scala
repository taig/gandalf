package io.taig.bsts

trait Validatable[T] {
    def validate( value: T ): Result[T]
}