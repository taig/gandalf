package test.io.taig.bsts

import io.taig.bsts._
import io.taig.bsts.default.show._
import io.taig.bsts.rule._

class Rule extends Suite {
    "Email" should "allow valid emails" in {
        Seq( "asdf@asdf.asdf", "asdf+34@asdf.asdf" ).foreach( email ⇒ {
            Email.validate( email ) shouldBe a[Success]
        } )
    }

    it should "not allow malformed emails" in {
        Seq( "", "asdf", "@asdf", "asdf@", "asdf@f", "asdf@fdsa", "asdf@asdf.a" ).foreach( email ⇒ {
            Email.validate( email ) shouldBe a[Failure]
        } )
    }

    "Exactly" should "allow exact size matches" in {
        Exactly[Array[_]]( 0 ).validate( Array.empty ) shouldBe a[Success]
        Exactly[Seq[_]]( 4 ).validate( Seq( "a", "s", "d", "f" ) ) shouldBe a[Success]
        Exactly[String]( 4 ).validate( "asdf" ) shouldBe a[Success]
    }

    it should "not allow divergent sizes" in {
        Exactly[String]( 0 ).validate( "asdf" ) shouldBe a[Failure]
        Exactly[Vector[_]]( 4 ).validate( Vector.empty ) shouldBe a[Failure]
    }

    "Matches" should "allow equality" in {
        Matches( "asdf" ).validate( "asdf" ) shouldBe a[Success]
        Matches( 123 ).validate( 123 ) shouldBe a[Success]
        Matches( 0x123 ).validate( 0x123 ) shouldBe a[Success]
    }

    it should "not allow inequality" in {
        Matches( "asdf" ).validate( "fdsa" ) shouldBe a[Failure]
        Matches( 123 ).validate( 321 ) shouldBe a[Failure]
        Matches( 0x123 ).validate( 0x321 ) shouldBe a[Failure]
    }

    "Max" should "allow lower size matches" in {
        Max[String]( 4 ).validate( "asdf" ) shouldBe a[Success]
        Max[String]( 4 ).validate( "" ) shouldBe a[Success]
        Max[Array[_]]( 0 ).validate( Array.empty ) shouldBe a[Success]
    }

    it should "not allow higher sizes" in {
        Max[String]( 3 ).validate( "asdf" ) shouldBe a[Failure]
        Max[String]( 0 ).validate( "1" ) shouldBe a[Failure]
        Max[Seq[_]]( 1 ).validate( Seq( 1, 2 ) ) shouldBe a[Failure]
    }

    "Min" should "allow higher size matches" in {
        Min[String]( 0 ).validate( "asdf" ) shouldBe a[Success]
        Min[String]( 4 ).validate( "asdf" ) shouldBe a[Success]
        Min[Array[_]]( 1 ).validate( Array( 1, 2 ) ) shouldBe a[Success]
    }

    it should "not allow lower sizes" in {
        Min[String]( 5 ).validate( "asdf" ) shouldBe a[Failure]
        Min[String]( 1 ).validate( "" ) shouldBe a[Failure]
        Min[Seq[_]]( 3 ).validate( Seq( 1, 2 ) ) shouldBe a[Failure]
    }

    "Phone" should "allow valid phone numbers" in {
        Seq( "0176123456", "+49265347765", "4444", "+49 265-347765", "+49.265.347765" ).foreach( phone ⇒ {
            Phone.validate( phone ) shouldBe a[Success]
        } )
    }

    it should "not allow malformed phone numbers" in {
        Seq( "", "110", "49+265347765", "0000" ).foreach( phone ⇒ {
            Phone.validate( phone ) shouldBe a[Failure]
        } )
    }

    "Required" should "allow non empty values" in {
        Required[String]().validate( "asdf " ) shouldBe a[Success]
        Required[Seq[_]]().validate( Seq( 1, 2, 3 ) ) shouldBe a[Success]
        Required[Option[Int]]().validate( Some( 3 ) ) shouldBe a[Success]
        Required[Option[_]]().validate( Some( 3 ) ) shouldBe a[Success]
    }

    it should "not allow empty values" in {
        Required[String]().validate( "" ) shouldBe a[Failure]
        Required[String]().validate( "  \t  " ) shouldBe a[Failure]
        Required[Array[String]]().validate( Array.empty ) shouldBe a[Failure]
        Required[Array[_]]().validate( Array.empty ) shouldBe a[Failure]
        Required[Option[Int]]().validate( None ) shouldBe a[Failure]
        Required[Option[_]]().validate( None ) shouldBe a[Failure]
    }
}