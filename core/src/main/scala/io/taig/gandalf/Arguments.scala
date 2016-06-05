package io.taig.gandalf

import shapeless.HList

trait Arguments {
    type Arguments <: HList
}