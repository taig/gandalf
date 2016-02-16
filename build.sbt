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
    .aggregate( core, rule, transformation, mutation, report, android )

lazy val core = project
    .settings( Settings.common )
    .settings(
        libraryDependencies ++=
            "com.chuusai" %% "shapeless" % "2.2.5" ::
            "org.typelevel" %% "cats" % "0.4.1" ::
            Nil
    )

lazy val rule = project
    .settings( Settings.common )
    .dependsOn( core )

lazy val transformation = project
    .settings( Settings.common )
    .dependsOn( core )

lazy val mutation = project
    .settings( Settings.common )
    .dependsOn( core )

lazy val report = project
    .settings( Settings.common )
    .dependsOn( core )

lazy val android = project
    .settings( androidBuildAar ++ Settings.common )
    .settings(
        libraryDependencies ++=
            "io.taig.android.viewvalue" %% "design" % "1.1.1" ::
            Nil,
        minSdkVersion := "7",
        platformTarget := "android-23",
        targetSdkVersion := "23",
        typedResources := false
    )
    .dependsOn( core, rule, transformation, mutation, report )

lazy val tests = project
    .settings( Settings.common )
    .settings(
        libraryDependencies ++=
            "org.scalatest" %% "scalatest" % "3.0.0-M15" ::
            Nil
    )
    .dependsOn( core, rule, transformation, mutation, report )