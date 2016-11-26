package io.taig.gandalf

package object core {
    type &&[L <: Rule, R <: Rule] = And[L, R]

    type ||[L <: Rule, R <: Rule] = Or[L, R]
}