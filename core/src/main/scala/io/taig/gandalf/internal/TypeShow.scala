package io.taig.gandalf.internal

trait TypeShow[T] {
    def show: String
}

object TypeShow {
    @inline
    def apply[T: TypeShow]: TypeShow[T] = implicitly[TypeShow[T]]

    def instance[T]( value: String ): TypeShow[T] = new TypeShow[T] {
        override def show = value
    }
}