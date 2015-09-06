package test.io.taig.bsts

import io.taig.bsts._
import org.scalacheck.Arbitrary.arbAnyVal
import org.scalatest.prop.GeneratorDrivenPropertyChecks

class TypeClass
        extends Suite
        with GeneratorDrivenPropertyChecks {
    def adjust[T]( implicit adjust: Adjust[Rule.Aux[T]] ) = adjust

    def empty[T]( implicit empty: Empty[Rule.Aux[T]] ) = empty

    def length[T: Length] = implicitly[Length[T]]

    "Adjust" should "trim String values" in {
        Seq( "", "   ", "\t", "  \t", "     \t  ", "  \t  \t  " ).foreach( blank ⇒ {
            adjust[String].adjust( blank ) shouldBe ""
        } )

        Seq( "asdf", "   asdf", "asdf   ", "\tasdf   ", "\t  asdf\t  " ).foreach( asdf ⇒ {
            adjust[String].adjust( asdf ) shouldBe "asdf"
        } )
    }

    it should "give back the identity for anything else" in forAll( arbAnyVal ) { any: Any ⇒
        adjust[Any].adjust( any ) shouldBe any
    }

    "Empty" should "work on Arrays" in {
        empty[Array[_]].isEmpty( Array.empty[Int] ) shouldBe true
        empty[Array[_]].isEmpty( Array( 1, 2, 3.4 ) ) shouldBe false
    }

    it should "work on Iterables" in {
        empty[Iterable[_]].isEmpty( Seq.empty[Int] ) shouldBe true
        empty[Iterable[_]].isEmpty( Seq( 1, 2, 3.4 ) ) shouldBe false
    }

    it should "work on Options" in {
        empty[Option[_]].isEmpty( None ) shouldBe true
        empty[Option[_]].isEmpty( Some( 3 ) ) shouldBe false
    }

    it should "work on Strings" in {
        empty[String].isEmpty( "" ) shouldBe true
        empty[String].isEmpty( "   " ) shouldBe false
        empty[String].isEmpty( "asdf" ) shouldBe false
    }

    it should "always be false for anything else" in forAll( arbAnyVal ) { any: Any ⇒
        empty[Any].isEmpty( any ) shouldBe false
    }

    "Length" should "work on Arrays" in {
        Map( Array.empty → 0, Array() → 0, Array( 1, 2, 3, 4 ) → 4, Array( 'a', 's', 'd', 'f' ) → 4 ).foreach {
            case ( value, expected ) ⇒ length[Array[_]].length( value ) shouldBe expected
        }
    }

    it should "work on Strings" in {
        Map( "" → 0, "asdf" → 4, "    " → 4, "a d " → 4 ).foreach {
            case ( value, expected ) ⇒ length[String].length( value ) shouldBe expected
        }
    }

    it should "work on Traversables" in {
        Map(
            Seq.empty → 0,
            Set() → 0,
            Vector( 1, 2, 3, 4 ) → 4,
            Map( 'a' → 's', 'd' → 'f', 'f' → 'd', 's' → 'a' ) → 4
        ).foreach { case ( value, expected ) ⇒ length[Traversable[_]].length( value ) shouldBe expected }
    }
}