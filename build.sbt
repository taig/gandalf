lazy val bsts = project.in( file( "." ) )
    .settings( Settings.common )
    .settings(
        aggregate in test := false,
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
        buildTypes += (
            "robolectric",
            Seq(
                fork in Test := true,
                libraryDependencies ++=
                    "com.geteit" %% "robotest" % "0.12" % "test" ::
                    Nil,
                libraryProject := false
            )
        ),
        libraryDependencies ++=
            "com.android.support" % "design" % "23.1.1" ::
            Nil,
        minSdkVersion := "7",
        organization += ".android",
        packageForR := organization.value + ".resources",
        platformTarget := "android-23",
        targetSdkVersion := "23",
        typedResources := false
    )
    .dependsOn( core % "compile->compile;test->test", rules, report )

addCommandAlias( "test", ";core/test;rules/test;report/test;variant robolectric;android/test" )