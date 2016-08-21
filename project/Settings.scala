import sbt._
import sbt.Keys._
import io.taig.sbt.sonatype.SonatypeHouserulePlugin.autoImport._

object Settings {
    val common = Def.settings(
        exportJars := true,
        javacOptions ++= (
            "-source" :: "1.7" ::
            "-target" :: "1.7" ::
            Nil
        ),
        githubProject := "gandalf",
        name := s"gandalf-${name.value}",
        organization := "io.taig",
        resolvers += Resolver.sonatypeRepo( "snapshots" ),
        scalacOptions ++=
            "-deprecation" ::
            "-feature" ::
            "-Xfuture" ::
            Nil,
        scalaVersion := "2.11.8",
        testOptions in ThisBuild += Tests.Argument( "-oDF" )
    )
}