package io.taig.bsts.rules.ops

trait Required[-T] {
    def exists( value: T ): Boolean
}

object Required {
    def apply[T]( f: T ⇒ Boolean ): Required[T] = new Required[T] {
        override def exists( value: T ) = f( value )
    }

    implicit def `Required[Option]`[T]: Required[Option[T]] = Required( _.nonEmpty )

    implicit val `Required[String]`: Required[CharSequence] = Required( _.length() > 0 )
}