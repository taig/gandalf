```tut:book
import io.taig.gandalf._; import implicits._; import predef.string._

val foobar = matches( "foobar" )
val bar = required ~> matches( "foobar" )
foobar.validate( "foobar" )
foobar.validate( "foo" )
foobar.validate( "" )
```