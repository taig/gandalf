package io.taig.bsts.ops.hlist

import shapeless._

trait Printer[L <: HList] extends DepFn2[Boolean, L] with Serializable {
    type Out = String

    def apply( tree: L ): String = apply( false, tree )
}

object Printer extends Printer0 {
    def apply[L <: HList]( implicit p: Printer[L] ): Printer[L] = p

    implicit def hnil: Printer[HNil] = new Printer[HNil] {
        override def apply( simple: Boolean, tree: HNil ) = simple match {
            case true  ⇒ ""
            case false ⇒ "HNil"
        }
    }

    implicit def hlist[H, T <: HList](
        implicit
        p1: Printer[H :: HNil],
        p2: Printer[T],
        ev: H <:!< HList
    ): Printer[( H :: HNil ) :: T] = new Printer[( H :: HNil ) :: T] {
        override def apply( simple: Boolean, tree: ( H :: HNil ) :: T ) = simple match {
            case true  ⇒ s"${p1( simple, tree.head )} ${p2( simple, tree.tail )}".trim
            case false ⇒ s"(${p1( simple, tree.head )}) :: ${p2( simple, tree.tail )}"
        }
    }
}

trait Printer0 extends Printer1 {
    implicit def recursion[H <: HList, T <: HList](
        implicit
        ph: Printer[H],
        pt: Printer[T]
    ): Printer[H :: T] = new Printer[H :: T] {
        override def apply( simple: Boolean, tree: H :: T ) = simple match {
            case true  ⇒ s"(${ph( simple, tree.head )}) ${pt( simple, tree.tail )}".trim
            case false ⇒ s"(${ph( simple, tree.head )}) :: ${pt( simple, tree.tail )}"
        }
    }
}

trait Printer1 {
    implicit def hcons[H, T <: HList](
        implicit
        p: Printer[T]
    ): Printer[H :: T] = new Printer[H :: T] {
        override def apply( simple: Boolean, tree: H :: T ) = simple match {
            case true  ⇒ s"${tree.head} ${p( simple, tree.tail )}".trim
            case false ⇒ s"${tree.head} :: ${p( simple, tree.tail )}"
        }
    }
}