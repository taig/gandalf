```tut:book
import io.taig.gandalf._; import implicits._; import core._; import predef.string._

val foobar = required && matches( "foobar" )
foobar.validate( "foobar" )
foobar.validate( "foo" )
foobar.validate( "" )
```