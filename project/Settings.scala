import sbt._
import sbt.Keys._
import io.taig.sbt.sonatype.SonatypeHouserulePlugin.autoImport._

object Settings {
    val common = Def.settings(
//        crossScalaVersions := Seq( "2.11.8", "2.12.1" ),
        exportJars := true,
        incOptions := incOptions.value.withLogRecompileOnMacro( false ),
        javacOptions ++= {
            scalaVersion.value match {
                case "2.11.8" =>
                    "-source" :: "1.7" ::
                    "-target" :: "1.7" ::
                    Nil
                case _ => Nil
            }
        },
        githubProject := "gandalf",
        name := s"gandalf-${name.value}",
        organization := "io.taig",
        resolvers ++=
            Resolver.sonatypeRepo( "snapshots" ) ::
            Nil,
        scalacOptions ++=
            "-deprecation" ::
            "-feature" ::
            "-Yliteral-types" ::
            "-Ypartial-unification" ::
            Nil,
        scalacOptions ++= {
            scalaVersion.value match {
                case "2.11.8" =>
                    "-target:jvm-1.7" ::
                    Nil
                case _ => Nil
            }
        },
        scalaOrganization := "org.typelevel",
        scalaVersion := "2.11.8",
        testOptions in ThisBuild += Tests.Argument( "-oDF" )
    )
}