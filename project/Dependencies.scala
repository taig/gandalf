import sbt._

object Dependencies {
    abstract class Configuration(
        group: String,
        version: String,
        prefix: Option[String] = None
    ) {
        def this( group: String, prefix: String, version: String ) = {
            this( group, version, Some( prefix ) )
        }

        def module(
            name: String,
            f: String => ModuleID
        ): ModuleID = {
            val artifact = prefix.map( _ + "-" ).getOrElse( "" ) + name
            f( artifact )
        }

        def scala( name: String, version: String = this.version ): ModuleID = {
            module( name, group %% _ % version )
        }

        def java( name: String, version: String = this.version ): ModuleID = {
            module( name, group % _ % version )
        }
    }

    object cats extends Configuration( "org.typelevel", "cats", "0.8.1" ) {
        val core = scala( "core" )

        val macros = scala( "macros" )
    }

    object circe extends Configuration( "io.circe", "circe", "0.6.0" ) {
        val core = scala( "core" )

        val generic = scala( "generic" )

        val parser = scala( "parser" )
    }

    val paradise = "org.scalamacros" % "paradise" % "2.1.0"

    val scalatest = "org.scalatest" %% "scalatest" % "3.0.1"

    val shapeless = "com.chuusai" %% "shapeless" % "2.3.2"
}