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
            "org.scalatest" %% "scalatest" % "3.0.0-M15" % "test" ::
            "org.scalacheck" %% "scalacheck" % "1.12.5" % "test" ::
            Nil
    )

lazy val rules = project
    .settings( Settings.common )
    .dependsOn( core % "compile->compile;test->test" )

lazy val report = project
    .settings( Settings.common )
    .dependsOn( core % "compile->compile;test->test" )

lazy val android = project
    .settings( buildWith( core, rules, report ) )
    .settings( androidBuildAar ++ Settings.common )
    .settings(
        libraryDependencies ++= (
            "com.android.support" % "design" % "23.1.1" ::
            Nil
        ),
        minSdkVersion := "7",
        organization += ".android",
        packageForR := organization.value + ".resources",
        platformTarget := "android-23",
        targetSdkVersion := "23",
        typedResources := false
    )
    .dependsOn( core, rules, report )

//lazy val androidTest = flavorOf( android, "android-test" )
//    .settings(
//        fork in Test := true,
//        libraryDependencies ++= (
//            "org.scalatest" %% "scalatest" % "2.2.5" % "test" ::
//            "com.geteit" %% "robotest" % "0.12" % "test" ::
//            Nil
//        ),
//        libraryProject in Android := false
//    )
//
//test in android <<= test in Test in androidTest