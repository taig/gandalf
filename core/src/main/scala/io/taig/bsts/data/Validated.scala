package io.taig.bsts.data

import io.taig.bsts.data.Validated.{ Invalid, Valid }

/**
 * A stripped down version of cats.data.Validated
 */
sealed abstract class Validated[+E, +A] extends Product with Serializable {
    def fold[B]( fe: E ⇒ B, fa: A ⇒ B ): B = this match {
        case Invalid( e ) ⇒ fe( e )
        case Valid( a )   ⇒ fa( a )
    }

    def isValid: Boolean = fold( _ ⇒ false, _ ⇒ true )

    def isInvalid: Boolean = fold( _ ⇒ true, _ ⇒ false )

    def foreach( f: A ⇒ Unit ): Unit = fold( _ ⇒ (), f )

    def getOrElse[B >: A]( default: ⇒ B ): B = fold( _ ⇒ default, identity )

    def exists( predicate: A ⇒ Boolean ): Boolean = fold( _ ⇒ false, predicate )

    def forall( f: A ⇒ Boolean ): Boolean = fold( _ ⇒ true, f )

    def orElse[EE, AA >: A]( default: ⇒ Validated[EE, AA] ): Validated[EE, AA] = this match {
        case v @ Valid( _ ) ⇒ v
        case Invalid( _ )   ⇒ default
    }

    def toEither: Either[E, A] = fold( Left.apply, Right.apply )

    def toOption: Option[A] = fold( _ ⇒ None, Some.apply )

    def toList: List[A] = fold( _ ⇒ Nil, List( _ ) )

    def bimap[EE, AA]( fe: E ⇒ EE, fa: A ⇒ AA ): Validated[EE, AA] = fold(
        fe andThen Invalid.apply,
        fa andThen Valid.apply
    )

    def map[B]( f: A ⇒ B ): Validated[E, B] = bimap( identity, f )

    def leftMap[EE]( f: E ⇒ EE ): Validated[EE, A] = bimap( f, identity )

    def foldLeft[B]( b: B )( f: ( B, A ) ⇒ B ): B = fold( _ ⇒ b, f( b, _ ) )

    def andThen[EE >: E, B]( f: A ⇒ Validated[EE, B] ): Validated[EE, B] = this match {
        case Valid( a )       ⇒ f( a )
        case i @ Invalid( _ ) ⇒ i
    }

    def swap: Validated[A, E] = this match {
        case Valid( a )   ⇒ Invalid( a )
        case Invalid( e ) ⇒ Valid( e )
    }
}

object Validated {
    final case class Valid[+A]( a: A ) extends Validated[Nothing, A]
    final case class Invalid[+E]( e: E ) extends Validated[E, Nothing]
}