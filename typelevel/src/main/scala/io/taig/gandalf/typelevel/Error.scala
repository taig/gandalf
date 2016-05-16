package io.taig.gandalf.typelevel

trait Error[-V <: Validation] {
    def error: String
}

object Error {
    def apply[V <: Validation: Error]: Error[V] = implicitly[Error[V]]

    def instance[V <: Validation]( message: String ): Error[V] = new Error[V] {
        override val error = message
    }

    implicit val error = Error.instance[Transformation]( "" )
}