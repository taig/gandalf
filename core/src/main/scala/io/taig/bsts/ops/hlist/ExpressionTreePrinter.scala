package io.taig.bsts.ops.hlist

import io.taig.bsts.Rule
import shapeless._

trait ExpressionTreePrinter[L <: HList] extends DepFn1[L] with Serializable { type Out = String }

object ExpressionTreePrinter extends LogicalExpressionTreePrinter0 {
    def apply[L <: HList]( implicit letp: ExpressionTreePrinter[L] ): ExpressionTreePrinter[L] = letp

    //    implicit def hnil: ExpressionTreePrinter[HNil] = new ExpressionTreePrinter[HNil] {
    //        override def apply( t: HNil ): Out = ""
    //    }
    //
    //    implicit def operationSimple[L <: Validation[_, _], O <: operator.Binary, R <: Validation[_, _]] = {
    //        new ExpressionTreePrinter[( L :: HNil ) :: O :: ( R :: HNil ) :: HNil] {
    //            override def apply( tree: ( L :: HNil ) :: O :: ( R :: HNil ) :: HNil ) = tree match {
    //                case ( l :: HNil ) :: o :: ( r :: HNil ) :: HNil ⇒ s"$l $o $r"
    //            }
    //        }
    //    }
    //
    //    implicit def operationLeft[L <: HList, O <: operator.Binary, R <: Validation[_, _]](
    //        implicit
    //        letp: ExpressionTreePrinter[L]
    //    ) = {
    //        new ExpressionTreePrinter[L :: O :: ( R :: HNil ) :: HNil] {
    //            override def apply( tree: L :: O :: ( R :: HNil ) :: HNil ) = tree match {
    //                case l :: o :: ( r :: HNil ) :: HNil ⇒ s"(${letp( l )}) $o $r"
    //            }
    //        }
    //    }
    //
    //    implicit def operationRight[L <: Validation[_, _], O <: operator.Binary, R <: HList](
    //        implicit
    //        letp: ExpressionTreePrinter[R]
    //    ) = {
    //        new ExpressionTreePrinter[( L :: HNil ) :: O :: R :: HNil] {
    //            override def apply( tree: ( L :: HNil ) :: O :: R :: HNil ) = tree match {
    //                case ( l :: HNil ) :: o :: r :: HNil ⇒ s"$l $o (${letp( r )})"
    //            }
    //        }
    //    }
    //
    //    implicit def operationDeep[L <: HList, O <: operator.Binary, R <: HList](
    //        implicit
    //        letpl: ExpressionTreePrinter[L],
    //        letpr: ExpressionTreePrinter[R]
    //    ): ExpressionTreePrinter[L :: O :: R] = {
    //        new ExpressionTreePrinter[L :: O :: R] {
    //            override def apply( tree: L :: O :: R ) = tree match {
    //                case l :: o :: r ⇒ s"(${letpl( l )}) $o (${letpr( r )})"
    //            }
    //        }
    //    }
}

trait LogicalExpressionTreePrinter0 extends LogicalExpressionTreePrinter1 {
    implicit def recursion[L <: HList, R <: HList](
        implicit
        letpl: ExpressionTreePrinter[L],
        letpr: ExpressionTreePrinter[R]
    ): ExpressionTreePrinter[L :: R] = new ExpressionTreePrinter[L :: R] {
        override def apply( tree: L :: R ) = s"${letpl( tree.head )}${letpr( tree.tail )}"
    }
}

trait LogicalExpressionTreePrinter1 {
    implicit def hcons[H, T <: HNil](
        implicit
        letp: ExpressionTreePrinter[T]
    ): ExpressionTreePrinter[H :: T] = {
        new ExpressionTreePrinter[H :: T] {
            override def apply( tree: H :: T ): Out = s"${tree.head}${letp( tree.tail )}"
        }
    }
}