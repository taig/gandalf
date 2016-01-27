package io.taig.bsts.android

import io.taig.bsts
import org.robolectric.RuntimeEnvironment
import org.scalatest.RobolectricSuite

trait Suite
        extends bsts.Suite
        with RobolectricSuite {
    implicit def context = RuntimeEnvironment.application
}