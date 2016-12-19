lazy val gandalf = project.in( file( "." ) )
    .settings( Settings.common )
    .settings(
        aggregate in test := false,
        name := "gandalf",
        organization := "io.taig",
        publish := (),
        publishArtifact := false,
        publishLocal := (),
        test := ( test in tests in Test ).value
    )
    .aggregate( core, predef, report, android )

lazy val core = project
    .settings( Settings.common )
    .settings(
        libraryDependencies ++=
            "com.chuusai" %% "shapeless" % "2.3.2" ::
            "org.typelevel" %% "cats-core" % "0.8.1" ::
            "org.typelevel" %% "cats-macros" % "0.8.1" ::
            Nil
    )

lazy val predef = project
    .settings( Settings.common )
    .dependsOn( core )

lazy val report = project
    .settings( Settings.common )
    .dependsOn( core )

lazy val android = project
    .enablePlugins( AndroidLib )
    .settings( Settings.common )
    .settings(
        libraryDependencies ++=
            "io.taig.android" %% "viewvalue" % "1.5.0" ::
            Nil,
        minSdkVersion := "9",
        platformTarget := "android-24",
        targetSdkVersion := "24",
        typedResources := false
    )
    .dependsOn( core, predef, report )

lazy val tests = project
    .settings( Settings.common )
    .settings(
        libraryDependencies ++=
            "org.scalatest" %% "scalatest" % "3.0.1" ::
            Nil
    )
    .dependsOn( core, predef, report )