import sbt._
import sbt.Keys._

object Settings {
    val common = Seq(
        javacOptions ++= (
            "-source" :: "1.7" ::
            "-target" :: "1.7" ::
            Nil
        ),
        organization := "io.taig.bsts",
        scalacOptions ++=
            "-deprecation" ::
            "-feature" ::
            Nil,
        scalaVersion := "2.11.7",
        testOptions in ThisBuild += Tests.Argument( "-oDF" )
    )
}