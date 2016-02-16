import io.taig.sbt.sonatype.Plugin.autoImport._
import sbt.Keys._
import sbt._

object Settings {
    val common = Seq(
        exportJars := true,
        javacOptions ++= (
            "-source" :: "1.7" ::
            "-target" :: "1.7" ::
            Nil
        ),
        githubProject := "bettersafethansorry",
        organization := "io.taig.bsts",
        scalacOptions ++=
            "-deprecation" ::
            "-feature" ::
            Nil,
        scalaVersion := "2.11.7",
        testOptions in ThisBuild += Tests.Argument( "-oDF" )
    )
}