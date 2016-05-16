package io.taig.gandalf.typelevel

case class &&[L <: Rule, R <: Rule.Aux[L#Input]](
    left:  Evaluation[L],
    right: Evaluation[R]
) extends Operation[L#Input]

object && {
    implicit def evaluation[L <: Rule, R <: Rule.Aux[L#Output]](
        implicit
        lev: Evaluation[L],
        ler: Error[L],
        rev: Evaluation[R],
        rer: Error[R],
        e:   Error[L && R]
    ) = {
        Evaluation.instance[L && R] { input ⇒
            ( lev.validate( input ) andThen rev.validate )
                .leftMap( e.error.map( List( _ ) ).getOrElse( _ ) )
        }
    }

    implicit val error = Error.none[_ && _]
}