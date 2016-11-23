package io.taig.gandalf

package object core {
    type &&[L <: Rule, R <: Rule] = L And R

    type ||[L <: Rule, R <: Rule] = L Or R
}