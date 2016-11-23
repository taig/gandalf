package io.taig.gandalf.core

import scala.PartialFunction.condOpt

sealed trait Validation[-R, I, O] {
    def apply( input: I ): Option[O]
}

object Validation {
    @inline
    def apply[R, I, O](
        implicit
        v: Validation[R, I, O]
    ): Validation[R, I, O] = v

    def instance[R, I, O](
        f: I ⇒ Option[O]
    ): Validation[R, I, O] = new Validation[R, I, O] {
        override def apply( input: I ): Option[O] = f( input )
    }

    trait Condition[-C, T] extends Validation[C, T, T] {
        override final def apply( input: T ): Option[T] = {
            condOpt( check( input ) ) { case true ⇒ input }
        }

        def check( input: T ): Boolean
    }

    object Condition {
        @inline
        def apply[C, T](
            implicit
            c: Condition[C, T]
        ): Condition[C, T] = c

        def instance[C, T]( f: T ⇒ Boolean ): Condition[C, T] = {
            new Condition[C, T] {
                @inline
                override def check( input: T ) = f( input )
            }
        }
    }

    trait Mutation[-M, I, O] extends Validation[M, I, O] {
        @inline
        override final def apply( input: I ): Option[O] = mutate( input )

        def mutate( input: I ): Option[O]
    }

    object Mutation {
        @inline
        def apply[M, I, O](
            implicit
            m: Mutation[M, I, O]
        ): Mutation[M, I, O] = m

        def instance[M, I, O](
            f: I ⇒ Option[O]
        ): Mutation[M, I, O] = new Mutation[M, I, O] {
            @inline
            override def mutate( input: I ): Option[O] = f( input )
        }
    }

    trait Transformation[-T, I, O] extends Validation[T, I, O] {
        @inline
        override final def apply( input: I ): Option[O] = {
            Some( transform( input ) )
        }

        def transform( input: I ): O
    }

    object Transformation {
        @inline
        def apply[T, I, O](
            implicit
            t: Transformation[T, I, O]
        ): Transformation[T, I, O] = t

        def instance[T, I, O](
            f: I ⇒ O
        ): Transformation[T, I, O] = new Transformation[T, I, O] {
            @inline
            override def transform( input: I ): O = f( input )
        }
    }
}