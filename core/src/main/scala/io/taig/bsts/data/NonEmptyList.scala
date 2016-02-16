package io.taig.bsts.data

import scala.language.higherKinds

final case class NonEmptyList[A]( head: A, tail: List[A] = Nil ) {
    def toList: List[A] = head +: tail

    override def toString = s"NonEmptyList(${( head +: tail ).mkString( "," )})"
}