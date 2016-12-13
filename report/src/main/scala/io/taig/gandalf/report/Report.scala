package io.taig.gandalf.report

import cats.data.NonEmptyList
import io.taig.gandalf._
import io.taig.gandalf.report.Report.static

import scala.language.{ existentials, higherKinds }
import scala.reflect._

trait Report[-R <: Rule, I, O] {
    def show( input: I ): NonEmptyList[String]
}

object Report extends Report0 {
    @inline
    def apply[R <: Rule, I, O](
        implicit
        r: Report[R, I, O]
    ): Report[R, I, O] = r

    implicit def not[R <: Rule, I, O](
        implicit
        r: Report[R, I, O]
    ): Report[not[R], I, O] = Report.instance { input ⇒
        r.show( input ).map( error ⇒ s"not($error)" )
    }

    implicit def symmetric[OP[L <: Rule, R <: Rule] <: Operator, L <: Rule, R <: Rule: ClassTag, I, O](
        implicit
        l: Report[L, I, O],
        r: Report[R, I, O]
    ): Report[OP[L, R], I, O] = {
        Report.instance { input ⇒
            l.show( input ) concat r.show( input )
        }
    }

    def instance[R <: Rule, I, O](
        f: I ⇒ NonEmptyList[String]
    ): Report[R, I, O] = new Report[R, I, O] {
        override def show( input: I ): NonEmptyList[String] = f( input )
    }

    def single[R <: Rule, I, O]( f: I ⇒ String ): Report[R, I, O] = {
        instance( f.andThen( NonEmptyList.of( _ ) ) )
    }

    def static[R <: Rule, I, O](
        message: String
    ): Report[R, I, O] = single[R, I, O]( _ ⇒ message )
}

trait Report0 {
    implicit def asymmetric[OP[L <: Rule, R <: Rule] <: Operator, L <: Rule, R <: Rule: ClassTag, I, O, P](
        implicit
        v: Validation[L, I, O],
        l: Report[L, I, O],
        r: Report[R, O, P]
    ): Report[OP[L, R], I, P] = {
        Report.instance { input ⇒
            val left = l.show( input )
            val right = v.confirm( input )
                .fold( List.empty[String] )( r.show( _ ).toList )

            left ++ right
        }
    }

    implicit def rule[R <: Rule: ClassTag, I, O]: Report[R, I, O] =
        static( classTag[R].runtimeClass.getSimpleName.replace( "$", "" ) )
}