package test.io.taig.bsts

import io.taig.bsts
import org.scalatest.{ Matchers, FlatSpec }

trait Suite
        extends FlatSpec
        with Matchers {
    type Failure = bsts.Failure[_]

    type Success = bsts.Success[_]
}