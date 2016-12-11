package io.taig

package object gandalf {
    type &&[L <: Rule, R <: Rule] = And[L, R]

    type ||[L <: Rule, R <: Rule] = Or[L, R]
}