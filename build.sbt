lazy val bsts = project.in( file( "." ) )
    .settings( Settings.common )
    .settings(
        name := "BetterSafeThanSorry",
        normalizedName := "better-safe-than-sorry",
        publishArtifact := false,
        testOptions in ThisBuild += Tests.Argument( "-oDF" )
    )
    .aggregate( core, rules )

lazy val core = project
    .settings( Settings.common )
    .settings(
        libraryDependencies ++=
            "com.chuusai" %% "shapeless" % "2.2.5" ::
            "org.scalatest" %% "scalatest" % "3.0.0-M15" % "test" ::
            "org.scalacheck" %% "scalacheck" % "1.12.5" % "test" ::
            Nil
    )

lazy val report = project
    .settings( Settings.common )
    .dependsOn( core % "compile->compile;test->test" )

lazy val rules = project
    .settings( Settings.common )
    .dependsOn( core % "compile->compile;test->test" )

//lazy val android = project.in( file( "android" ) )
//    .settings( androidBuildAar ++ Settings.common )
//    .settings(
//        javacOptions ++= (
//            "-source" :: "1.7" ::
//            "-target" :: "1.7" ::
//            Nil
//        ),
//        libraryDependencies ++= (
//            "com.android.support" % "design" % "23.1.1" ::
//            Nil
//        ),
//        organization += ".android"
//    )
//    .settings(
//        minSdkVersion := "7",
//        platformTarget := "android-23",
//        targetSdkVersion := "23",
//        typedResources := false
//    )
//    .dependsOn( core )
//
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