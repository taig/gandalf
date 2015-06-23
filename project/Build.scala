import sbt._
import sbt.Keys._
import android.Keys._
import android.Plugin._
import xerial.sbt.Sonatype._
import xerial.sbt.Sonatype.SonatypeKeys._

object	Build
extends	android.AutoBuild
{
	val main = Project( "bettersafethansorry", file( "." ), settings = androidBuildAar ++ sonatypeSettings )
		.settings(
			fork in Test := true,
			javacOptions ++= (
				"-source" :: "1.7" ::
				"-target" :: "1.7" ::
				Nil
			),
			libraryDependencies ++= (
				"com.android.support" % "appcompat-v7" % "22.2.0" ::
				"com.android.support" % "support-v4" % "22.2.0" ::
				"com.android.support" % "design" % "22.2.0" ::
				"com.geteit" %% "robotest" % "0.10" % "test" ::
				"org.scalatest" %% "scalatest" % "2.2.5" % "test" ::
				Nil
			),
			name := "BetterSafeThanSorry",
			organization := "io.taig.android",
			resolvers ++= (
				( "RoboTest releases" at "https://raw.github.com/zbsz/mvn-repo/master/releases/" ) ::
				Resolver.sonatypeRepo( "snapshots" ) ::
				Nil
			),
			scalaVersion := "2.11.6",
			scalacOptions ++= (
				"-deprecation" ::
				"-feature" ::
				Nil
			),
			version := "1.0.0-SNAPSHOT"
		)
		.settings(
			minSdkVersion in Android := "8",
			platformTarget in Android := "android-22",
			sourceGenerators in Compile <<= ( sourceGenerators in Compile ) ( generators => Seq( generators.last ) ),
			targetSdkVersion in Android := "22",
			transitiveAndroidLibs in Android := false,
			typedResources in Android := false
		)
		.settings(
			description := "Form validation for Scala on Android",
			homepage := Some( url( "https://github.com/taig/bettersafethansorry" ) ),
			licenses := Seq( "MIT" -> url( "https://raw.githubusercontent.com/taig/bettersafethansorry/master/LICENSE" ) ),
			organizationHomepage := Some( url( "http://taig.io" ) ),
			pomExtra :=
			{
				<issueManagement>
					<url>https://github.com/taig/bettersafethansorry/issues</url>
					<system>GitHub Issues</system>
				</issueManagement>
				<developers>
					<developer>
						<id>Taig</id>
						<name>Niklas Klein</name>
						<email>mail@taig.io</email>
						<url>http://taig.io/</url>
					</developer>
				</developers>
			},
			pomIncludeRepository := { _ => false },
			publishArtifact in Test := false,
			publishMavenStyle := true,
			publishTo <<= version ( version =>
			{
				val url = Some( "https://oss.sonatype.org/" )

				if( version.endsWith( "SNAPSHOT" ) )
				{
					url.map( "snapshot" at _ + "content/repositories/snapshots" )
				}
				else
				{
					url.map( "release" at _ + "service/local/staging/deploy/maven2" )
				}
			} ),
			scmInfo := Some(
				ScmInfo(
					url( "https://github.com/taig/bettersafethansorry" ),
					"scm:git:git://github.com/taig/bettersafethansorry.git",
					Some( "scm:git:git@github.com:taig/bettersafethansorry.git" )
				)
			),
			sonatypeProfileName := "io.taig",
			startYear := Some( 2015 )
		)
}
