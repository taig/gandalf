package test.io.taig.android.bsts

import org.robolectric.RuntimeEnvironment
import org.scalatest.{ FlatSpec, Matchers, RobolectricSuite }

trait Suite
        extends FlatSpec
        with Matchers
        with RobolectricSuite {
    implicit def context = RuntimeEnvironment.application
}