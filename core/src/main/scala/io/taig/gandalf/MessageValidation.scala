package io.taig.gandalf

case class MessageValidation[V <: Validation]( validation: V, error: Error[V] )