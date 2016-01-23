package io.taig.bsts

import io.taig.bsts
import shapeless._
import shapeless.ops.hlist.ToTraversable

trait Rule[I <: String, T, A <: HList] {
    def apply( value: T ): Either[Error[I, A], T]

    def validate( value: T ): Rule.Validation[I, A] = Rule.Validation( apply( value ).left.toOption )
}

object Rule {
    /**
     * Create a simple Rule where the input value can be validated without prior transformation and the list of
     * supplied error arguments is empty
     *
     * Example:
     * {{{
     * val required = Rule[String]( "required" )( _.nonEmpty )
     * }}}
     *
     * @param identifier Name of the rule
     * @tparam T Input type to be validated
     * @return [[io.taig.bsts.Rule.Builder0]] object which provides an <code>apply</code> method to finalize rule
     *         creation
     */
    def empty[T]( identifier: String ): Builder0[identifier.type, T] = new Builder0

    class Builder0[I <: String, T] {
        /**
         * Lorem Ipsum
         *
         * @param predicate Function to validate the input value
         * @param w
         * @return
         */
        def apply( predicate: T ⇒ Boolean )(
            implicit
            w:  Witness.Aux[I],
            tt: ToTraversable.Aux[HNil, List, Any]
        ): Rule[I, T, HNil] = new Rule[I, T, HNil] {
            override def apply( value: T ): Either[Error[I, HNil], T] = {
                new Builder1[I, T]()( predicate )( _ ⇒ HNil: HNil ).apply( value )
            }
        }
    }

    /**
     * Create a simple Rule where the input value can be validated without prior transformation
     *
     * Example:
     * {{{
     * def match[T]( compare: T ) = Rule[T]( "match" )( _ == compare ) { value ⇒
     *     "expected" ->> expected :: "actual" ->> value :: HNil )
     * }
     * }}}
     *
     * @param identifier Name of the rule
     * @tparam T Input type to be validated
     * @return [[io.taig.bsts.Rule.Builder1]] object which provides an <code>apply</code> method to finalize rule
     *         creation
     */
    def apply[T]( identifier: String ): Builder1[identifier.type, T] = new Builder1()

    /**
     * Apply method helper to avoid specifying the Arguments type explicitly
     */
    class Builder1[I <: String, T] {
        /**
         * Lorem Ipsum
         *
         * @param predicate Function to validate the input value
         * @param error
         * @param w
         * @tparam A
         * @return
         */
        def apply[A <: HList]( predicate: T ⇒ Boolean )( error: T ⇒ A )(
            implicit
            w:  Witness.Aux[I],
            tt: ToTraversable.Aux[A, List, Any]
        ): Rule[I, T, A] = new Rule[I, T, A] {
            override def apply( value: T ): Either[Error[I, A], T] = {
                new Builder2[I, T, T]()( identity )( predicate )( ( value, _ ) ⇒ error( value ) ).apply( value )
            }
        }
    }

    /**
     * Create a Rule where the input value needs to be transformed before validation
     *
     * Example:
     * {{{
     * def min( length: Int ) = Rule[String, Int]( "min" )( _.length )( _ <= length ) { ( value, actual ) ⇒
     *     "value" ->> value :: "expected" ->> length :: "actual" ->> actual :: HNil
     * }
     * }}}
     *
     * @param identifier Name of the rule
     * @tparam T Input type to be validated
     * @tparam S Transformation type
     * @return [[io.taig.bsts.Rule.Builder2]] object which provides an <code>apply</code> method to finalize rule
     *         creation
     */
    def apply[T, S]( identifier: String ): Builder2[identifier.type, T, S] = new Builder2

    /**
     * Apply method helper to avoid specifying the Arguments type explicitly
     */
    class Builder2[I <: String, T, S] {
        /**
         * Lorem Ipsum
         *
         * @param transformation Function to transform the input value to value that matters for validation (e.g.
         *                       getting the length of a String)
         * @param predicate      Function to validate the input value
         * @param error
         * @param w
         * @tparam A
         * @return
         */
        def apply[A <: HList]( transformation: T ⇒ S )( predicate: S ⇒ Boolean )( error: ( T, S ) ⇒ A )(
            implicit
            w:  Witness.Aux[I],
            tt: ToTraversable.Aux[A, List, Any]
        ): Rule[I, T, A] = new Rule[I, T, A] {
            override def apply( value: T ): Either[Error[I, A], T] = {
                val computed = transformation( value )

                predicate( computed ) match {
                    case true  ⇒ Right( value )
                    case false ⇒ Left( Error( error( value, computed ) ) )
                }
            }
        }
    }

    case class Validation[I <: String, A <: HList]( result: Option[Error[I, A]] )
            extends bsts.Validation[Report[I, A], Option[String], Option[( String, List[Any] )]] {
        override def isSuccess = result.isEmpty

        override def report( implicit r: Report[I, A] ): Option[String] = result.map( _.report )

        override def raw: Option[( String, List[Any] )] = result.map( _.raw )

        override def toString = raw match {
            case Some( ( identifier, Nil ) )       ⇒ s"Failure($identifier)"
            case Some( ( identifier, arguments ) ) ⇒ s"Failure($identifier, (${arguments.mkString( ", " )}))"
            case None                              ⇒ "Success"
        }
    }
}