package io.taig.gandalf

import shapeless.HList

trait ErrorArguments {
    type Arguments <: HList
}