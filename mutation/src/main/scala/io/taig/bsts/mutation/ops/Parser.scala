package io.taig.bsts.mutation.ops

import scala.language.higherKinds
import scala.util.Try

trait Parser[T] {
    def parse( input: String ): Try[T]
}

object Parser {
    implicit val `Parser[Double]`: Parser[Double] = new Parser[Double] {
        override def parse( input: String ) = Try( input.toDouble )
    }

    implicit val `Parser[Float]`: Parser[Float] = new Parser[Float] {
        override def parse( input: String ) = Try( input.toFloat )
    }

    implicit val `Parser[Int]`: Parser[Int] = new Parser[Int] {
        override def parse( input: String ) = Try( input.toInt )
    }

    implicit val `Parser[Long]`: Parser[Long] = new Parser[Long] {
        override def parse( input: String ) = Try( input.toLong )
    }
}