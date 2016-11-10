lazy val gandalf = project.in( file( "." ) )
    .settings( Settings.common ++ tutSettings )
    .settings(
        name := "gandalf"
    )
    .aggregate( core, macros, predef, circe )
    .dependsOn( core, macros, predef, circe )

lazy val core = project
    .settings( Settings.common )
    .settings(
        libraryDependencies ++=
            Dependencies.shapeless ::
            Dependencies.cats.core ::
            Dependencies.cats.macros ::
            Dependencies.scalatest % "test" ::
            Nil
    )

lazy val macros = project
    .settings( Settings.common )
    .settings(
        addCompilerPlugin( Dependencies.scalameta.paradise cross CrossVersion.full ),
        libraryDependencies ++=
            Dependencies.scalameta.scalameta % "provided" ::
            Nil,
        scalacOptions += "-Xplugin-require:macroparadise",
        // temporary workaround for https://github.com/scalameta/paradise/issues/10
        scalacOptions in (Compile, console) += "-Yrepl-class-based", // necessary to use console
        // temporary workaround for https://github.com/scalameta/paradise/issues/55
        sources in (Compile, doc) := Nil
    )
    .dependsOn( core % "compile->compile;test->test" )

lazy val predef = project
    .settings( Settings.common )
    .dependsOn( core % "compile->compile;test->test" )

lazy val circe = project
    .settings( Settings.common )
    .settings(
        addCompilerPlugin( Dependencies.scalameta.paradise cross CrossVersion.full ),
        libraryDependencies ++=
            Dependencies.circe.core ::
            Dependencies.scalameta.scalameta % "provided" ::
            Dependencies.circe.generic % "test" ::
            Dependencies.circe.parser % "test" ::
            Nil,
        scalacOptions += "-Xplugin-require:macroparadise",
        // temporary workaround for https://github.com/scalameta/paradise/issues/10
        scalacOptions in (Compile, console) += "-Yrepl-class-based", // necessary to use console
        // temporary workaround for https://github.com/scalameta/paradise/issues/55
        sources in (Compile, doc) := Nil
    )
    .dependsOn(
        core % "compile->compile;test->test",
        macros % "compile->test",
        predef % "compile->test"
    )

//lazy val android = project
//    .settings( androidBuildAar ++ Settings.common )
//    .settings(
//        libraryDependencies ++=
//            "io.taig.android" %% "viewvalue-core" % "1.2.4" ::
//            Nil,
//        minSdkVersion := "1",
//        platformTarget := "android-23",
//        targetSdkVersion := "23",
//        typedResources := false
//    )
//    .dependsOn( core, predef, show )