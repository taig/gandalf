package io.taig.bsts

import io.taig.bsts.ops.hlist.Printer
import shapeless._

class PrinterTest extends Suite {
    it should "serialize nested HLists" in {
        def print[H <: HList]( h: H )( implicit p: Printer[H] ) = p( h )

        print( 1 :: 2 :: ( "foo" :: 3 :: ( "bar" :: HNil ) :: HNil ) :: HNil ) shouldBe
            "1 :: 2 :: (foo :: 3 :: (bar :: HNil) :: HNil) :: HNil"
    }
}