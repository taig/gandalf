lazy val gandalf = project.in( file( "." ) )
    .settings( Settings.common )
    .settings(
        name := "gandalf"
    )
    .aggregate( core, predef )
    .dependsOn( core, predef )

lazy val core = project
    .settings( Settings.common )
    .settings(
        addCompilerPlugin( "org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full ),
        libraryDependencies ++=
            "com.chuusai" %% "shapeless" % "2.3.2" ::
            "org.scala-lang" % "scala-reflect" % scalaVersion.value ::
            "org.typelevel" %% "cats-core" % "0.7.2" ::
            "org.typelevel" %% "cats-macros" % "0.7.2" ::
            "org.scalatest" %% "scalatest" % "3.0.0" % "test" ::
            Nil
    )

lazy val predef = project
    .settings( Settings.common )
    .settings(
        addCompilerPlugin( "org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full )
    )
    .dependsOn( core % "compile->compile;test->test" )

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
//    .dependsOn( core, predef, report )