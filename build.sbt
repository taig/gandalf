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
    .aggregate( core, predef, report, android )

lazy val core = project
    .settings( Settings.common )
    .settings(
        libraryDependencies ++=
            "com.chuusai" %% "shapeless" % "2.3.0" ::
            "org.typelevel" %% "cats-core" % "0.5.0" ::
            "org.typelevel" %% "cats-macros" % "0.5.0" ::
            Nil,
        name := "gandalf-core"
    )

lazy val predef = project
    .settings( Settings.common )
    .settings(
        name := "gandalf-predef"
    )
    .dependsOn( core )

lazy val report = project
    .settings( Settings.common )
    .settings(
        name := "gandalf-report"
    )
    .dependsOn( core )

lazy val android = project
    .settings( androidBuildAar ++ Settings.common )
    .settings(
        libraryDependencies ++=
            "io.taig.android" %% "viewvalue-core" % "1.2.3" ::
            Nil,
        minSdkVersion := "1",
        name := "gandalf-android",
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