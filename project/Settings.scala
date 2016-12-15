import sbt._
import sbt.Keys._
import io.taig.sbt.sonatype.SonatypeHouserulePlugin.autoImport._

object Settings {
    val common = Def.settings(
//        crossScalaVersions := Seq( "2.11.8", "2.12.0" ),
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
        scalacOptions ++=
            "-deprecation" ::
            "-encoding" :: "UTF-8" ::
            "-feature" ::
            "-unchecked" ::
            // TODO "-Xfatal-warnings" ::
            "-Xfuture" ::
            // TODO "-Xlint" ::
            "-Yliteral-types" ::
            "-Yno-adapted-args" ::
            "-Ypartial-unification" ::
            "-Ywarn-unused-import" ::
            "-Ywarn-dead-code" ::
            "-Ywarn-numeric-widen" ::
            "-Ywarn-value-discard" ::
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