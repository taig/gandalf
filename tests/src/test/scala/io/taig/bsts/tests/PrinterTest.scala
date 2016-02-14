package io.taig.bsts.tests

import io.taig.bsts.ops.hlist.Printer
import shapeless._

class PrinterTest extends Suite {
    def print[H <: HList]( h: H, simple: Boolean )( implicit p: Printer[H] ) = p( simple, h )

    it should "serialize nested HLists" in {
        print( 1 :: 2 :: ( "foo" :: 3 :: ( "bar" :: HNil ) :: HNil ) :: HNil, false ) shouldBe
            "1 :: 2 :: (foo :: 3 :: (bar :: HNil) :: HNil) :: HNil"
    }

    it should "serialize nested HLists in a simplified version" in {
        print( 1 :: 2 :: ( "foo" :: 3 :: ( "bar" :: HNil ) :: HNil ) :: HNil, true ) shouldBe "1 2 (foo 3 bar)"
    }
}