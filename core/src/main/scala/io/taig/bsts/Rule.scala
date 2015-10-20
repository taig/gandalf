package io.taig.bsts

trait Rule {
    type Value
}

object Rule {
    type Aux[T] = Rule { type Value = T }
}

package operation.rule {
    import io.taig.bsts

    abstract class Validatable[R <: Rule]( rule: R )(
            implicit
            definition: Definition[R],
            show:       Show[R],
            adjust:     Adjust[R]
    ) extends bsts.Validatable[R#Value] {
        override def validate( value: R#Value ): Result[R#Value] = {
            val adjusted = adjust.adjust( value )

            definition( adjusted, rule ) match {
                case true  ⇒ Success( adjusted )
                case false ⇒ Failure( adjusted, Seq( show.show( adjusted, rule ) ) )
            }
        }
    }
}