lazy val bsts = project.in( file( "." ) )
    .settings( Settings.common )
    .settings(
        name := "BetterSafeThanSorry",
        normalizedName := "better-safe-than-sorry",
        organization := "io.taig",
        publishArtifact := false
    )
    .aggregate( core, rules, report, android )

lazy val core = project
    .settings( Settings.common )
    .settings(
        libraryDependencies ++=
            "com.chuusai" %% "shapeless" % "2.2.5" ::
            "org.scala-lang" % "scala-reflect" % scalaVersion.value % "compile" ::
            "org.scalatest" %% "scalatest" % "3.0.0-M15" % "test" ::
            Nil
    )

lazy val rules = project
    .settings( Settings.common )
    .dependsOn( core % "compile->compile;test->test" )

lazy val report = project
    .settings( Settings.common )
    .dependsOn( core % "compile->compile;test->test" )

lazy val android = project
    .settings( androidBuildAar ++ Settings.common )
    .settings(
        libraryDependencies ++=
            "com.android.support" % "design" % "23.1.1" ::
            "com.geteit" %% "robotest" % "0.12" % "test" ::
            Nil,
        minSdkVersion := "7",
        packageForR := organization.value + ".android.resources",
        platformTarget := "android-23",
        targetSdkVersion := "23",
        test := {},
        typedResources := false
    )
    .dependsOn( core % "compile->compile;test->test", rules, report )