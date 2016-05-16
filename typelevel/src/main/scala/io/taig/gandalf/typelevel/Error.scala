package io.taig.gandalf.typelevel

trait Error[-V <: Validation] {
    def error: Option[String]
}

object Error {
    def apply[V <: Validation: Error]: Error[V] = implicitly[Error[V]]

    def instance[V <: Validation]( message: String ): Error[V] = new Error[V] {
        override val error = Some( message )
    }

    def none[V <: Validation]: Error[V] = new Error[V] {
        override val error = None
    }

    implicit val transformation = none[Transformation]
}