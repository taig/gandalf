import sbt._
import sbt.Keys._

object Settings {
    val common = Def.settings(
        libraryDependencies ++=
            compilerPlugin( "org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full ) ::
            "org.scala-lang" % "scala-reflect" % scalaVersion.value % "provided" ::
            "org.typelevel" %% "export-hook" % "1.1.0" ::
            Nil,
        organization := "io.taig",
        scalacOptions ++=
            "-deprecation" ::
            "-feature" ::
            Nil,
        scalaVersion := "2.11.7",
        version := "0.3.0-SNAPSHOT"
    )
}