lazy val gandalf = project.in( file( "." ) )
    .settings( Settings.common ++ tutSettings )
    .settings(
        name := "gandalf"
    )
    .aggregate( core, macros, predef, circe, doobie, android )
    .dependsOn( core, macros, predef )

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

lazy val macros = project
    .settings( Settings.common )
    .settings(
        addCompilerPlugin( Dependencies.paradise cross CrossVersion.full )
    )
    .dependsOn( core % "compile->compile;test->test" )

lazy val predef = project
    .settings( Settings.common )
    .dependsOn( core % "compile->compile;test->test" )

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
        macros % "compile->test",
        predef % "compile->test"
    )

lazy val doobie = project
    .settings( Settings.common )
    .settings(
        libraryDependencies ++=
            Dependencies.doobie ::
            Nil
    )
    .dependsOn(
        core % "compile->compile;test->test",
        macros % "compile->test",
        predef % "compile->test"
    )

lazy val android = project
    .enablePlugins( AndroidLib )
    .settings( Settings.common )
    .settings(
        minSdkVersion := "1",
        platformTarget := "android-24",
        targetSdkVersion := "24",
        typedResources := false
    )
    .dependsOn( core % "compile->compile;test->test" )