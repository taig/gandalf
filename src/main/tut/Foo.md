```tut:book
import io.taig.gandalf._; import implicits._; import predef._

val foobar = required && matches( "foobar" )
"foobar".confirm( foobar )
"foo".confirm( foobar )
// foobar.validate( "foo" )
// foobar.validate( "" )
```