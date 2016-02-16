package io.taig.bsts.syntax

import cats.FlatMap

trait validated {
    implicit def yolo = new FlatMap[] {}
}

object validated extends validated