lazy val bsts = project.in( file( "." ) )
    .settings( Settings.common )
    .settings(
        aggregate in test := false,
        name := "BetterSafeThanSorry",
        normalizedName := "better-safe-than-sorry",
        organization := "io.taig",
        publishArtifact := false,
        test <<= test in tests in Test
    )
    .aggregate( core, predef, report, android )

lazy val core = project
    .settings( Settings.common )
    .settings(
        libraryDependencies ++=
            "com.chuusai" %% "shapeless" % "2.2.5" ::
            "org.typelevel" %% "cats-core" % "0.4.1" ::
            "org.typelevel" %% "cats-macros" % "0.4.1" ::
            Nil
    )

lazy val predef = project
    .settings( Settings.common )
    .dependsOn( core )

lazy val report = project
    .settings( Settings.common )
    .dependsOn( core )

lazy val android = project
    .settings( androidBuildAar ++ Settings.common )
    .settings(
        libraryDependencies ++=
            "io.taig.android.viewvalue" %% "design" % "1.2.0-SNAPSHOT" ::
            Nil,
        minSdkVersion := "7",
        platformTarget := "android-23",
        targetSdkVersion := "23",
        typedResources := false
    )
    .dependsOn( core, predef, report )

lazy val tests = project
    .settings( Settings.common )
    .settings(
        libraryDependencies ++=
            "org.scalatest" %% "scalatest" % "3.0.0-M15" ::
            Nil
    )
    .dependsOn( core, predef, report )