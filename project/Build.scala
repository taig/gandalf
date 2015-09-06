import sbt.Keys._
import sbt._
import android._
import android.Plugin._
import android.Keys._

object Build extends sbt.Build {
    lazy val common = Seq(
        name := "BetterSafeThanSorry",
        normalizedName := "better-safe-than-sorry",
        organization := "io.taig",
        scalacOptions ++= (
            "-deprecation" ::
            "-feature" ::
            Nil
        ),
        scalaVersion := "2.11.7",
        version := "1.0.1-SNAPSHOT"
    )

    lazy val bsts = project.in( file( "." ) )
        .settings( common )
        .settings(
            publishArtifact := false
        )
        .aggregate( core, android )

    lazy val core = project.in( file( "core" ) )
        .settings( common )
        .settings(
            libraryDependencies ++= (
                "com.chuusai" %% "shapeless" % "2.2.5" ::
                "org.spire-math" %% "cats-macros" % "0.2.0" ::
                "org.spire-math" %% "cats-core" % "0.2.0" ::
                "org.scalatest" %% "scalatest" % "3.0.0-M7" % "test" ::
                "org.scalacheck" %% "scalacheck" % "1.12.4" % "test" ::
                Nil
            )
        )

    lazy val android = project.in( file( "android" ) )
        .settings( androidBuildAar )
        .settings( common )
        .settings(
            javacOptions ++= (
                "-source" :: "1.7" ::
                "-target" :: "1.7" ::
                Nil
            ),
            libraryDependencies ++= (
                "com.android.support" % "design" % "23.0.1" ::
                Nil
            ),
            organization += ".android"
        )
        .settings( inConfig( Android )( Seq(
            minSdkVersion := "7",
            platformTarget := "android-23",
            targetSdkVersion := "23",
            typedResources := false
        ) ) )
        .dependsOn( core )
    
    lazy val androidTest = flavorOf( android, "android-test" )
        .settings(
            fork in Test := true,
            libraryDependencies ++= (
                "org.scalatest" %% "scalatest" % "2.2.5" % "test" ::
                "com.geteit" %% "robotest" % "0.12" % "test" ::
                Nil
            ),
            libraryProject in Android := false
        )
}