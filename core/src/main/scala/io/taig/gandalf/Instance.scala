package io.taig.gandalf

trait Instance[A <: Action] {
    def get: A
}