import sbt._
import sbt.Keys._
import io.taig.sbt.sonatype.SonatypeHouserulePlugin.autoImport._

object Settings {
    val common = Def.settings(
        exportJars := true,
        incOptions := incOptions.value.withLogRecompileOnMacro( false ),
        javacOptions ++= (
            "-source" :: "1.7" ::
            "-target" :: "1.7" ::
            Nil
        ),
        githubProject := "gandalf",
        name := s"gandalf-${name.value}",
        organization := "io.taig",
        resolvers ++=
            Resolver.sonatypeRepo( "snapshots" ) ::
            Resolver.url(
                "scalameta",
                url( "http://dl.bintray.com/scalameta/maven" )
            )( Resolver.ivyStylePatterns ) ::
            Nil,
        scalacOptions ++=
            "-deprecation" ::
            "-feature" ::
            "-Yliteral-types" ::
            "-Ypartial-unification" ::
            Nil,
        scalaOrganization := "org.typelevel",
        scalaVersion := "2.11.8",
        testOptions in ThisBuild += Tests.Argument( "-oDF" )
    )
}