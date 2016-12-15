lazy val gandalf = project.in( file( "." ) )
    .settings( Settings.common ++ tutSettings )
    .settings(
        name := "gandalf"
    )
    .aggregate( core, report, macros, predef, circe )
    .dependsOn( core, report, macros, predef )

lazy val core = project
    .settings( Settings.common )
    .settings(
        libraryDependencies ++=
            Dependencies.cats.core ::
            Dependencies.cats.macros ::
            Dependencies.shapeless ::
            Dependencies.scalatest % "test" ::
            Nil
    )

lazy val report = project
    .settings( Settings.common )
    .dependsOn( core % "compile->compile;test->test" )

lazy val macros = project
    .settings( Settings.common )
    .settings(
        addCompilerPlugin( Dependencies.paradise cross CrossVersion.full )
    )
    .dependsOn( core % "compile->compile;test->test" )

lazy val predef = project
    .settings( Settings.common )
    .settings(
        addCompilerPlugin( Dependencies.paradise cross CrossVersion.full )
    )
    .dependsOn( macros % "compile->compile;test->test" )

lazy val circe = project
    .settings( Settings.common )
    .settings(
        addCompilerPlugin( Dependencies.paradise % "test" cross CrossVersion.full ),
        libraryDependencies ++=
            Dependencies.circe.core ::
            Dependencies.circe.parser ::
            Dependencies.circe.generic % "test" ::
            Nil
    )
    .dependsOn(
        core % "compile->compile;test->test",
        report,
        macros % "compile->test",
        predef % "compile->test"
    )

//lazy val android = project
//    .enablePlugins( AndroidLib )
//    .settings( Settings.common )
//    .settings(
//        minSdkVersion := "1",
//        platformTarget := "android-24",
//        targetSdkVersion := "24",
//        typedResources := false
//    )
//    .dependsOn( core, predef )