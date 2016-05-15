lazy val gandalf = project.in( file( "." ) )
    .settings( Settings.common )
    .settings(
        aggregate in test := false,
        name := "Gandalf",
        organization := "io.taig",
        publish := (),
        publishArtifact := false,
        publishLocal := (),
        test <<= test in tests in Test
    )
    .aggregate( core, predef, report, typelevel, android )

lazy val core = project
    .settings( Settings.common )
    .settings(
        libraryDependencies ++=
            "com.chuusai" %% "shapeless" % "2.3.1" ::
            "org.typelevel" %% "cats-core" % "0.6.0" ::
            "org.typelevel" %% "cats-macros" % "0.6.0" ::
            Nil
    )

lazy val predef = project
    .settings( Settings.common )
    .dependsOn( core )

lazy val report = project
    .settings( Settings.common )
    .dependsOn( core )

lazy val typelevel = project
    .settings( Settings.common )
    .dependsOn( core )

lazy val playground = project
    .settings( Settings.common )
    .dependsOn( typelevel )

lazy val android = project
    .settings( androidBuildAar ++ Settings.common )
    .settings(
        libraryDependencies ++=
            "io.taig.android" %% "viewvalue-core" % "1.2.5" ::
            Nil,
        minSdkVersion := "1",
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