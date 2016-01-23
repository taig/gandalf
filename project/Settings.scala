import sbt.Keys._

object Settings {
    val common = Seq(
        organization := "io.taig",
        scalacOptions ++= (
            "-deprecation" ::
            "-feature" ::
            Nil
        ),
        scalaVersion := "2.11.7",
        version := "0.2.0"
    )
}