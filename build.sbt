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
    version := "1.0.2-SNAPSHOT"
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
            "org.scalatest" %% "scalatest" % "3.0.0-M7" % "test" ::
            "org.scalacheck" %% "scalacheck" % "1.12.5" % "test" ::
            Nil
        )
    )

lazy val android = project.in( file( "android" ) )
    .settings( androidBuildAar ++ common )
    .settings(
        javacOptions ++= (
            "-source" :: "1.7" ::
            "-target" :: "1.7" ::
            Nil
        ),
        libraryDependencies ++= (
            "com.android.support" % "design" % "23.1.0" ::
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

test in android <<= test in Test in androidTest