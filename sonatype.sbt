val id = "bettersafethansorry"

description in ThisBuild := "Form validation for Scala on Android"

homepage in ThisBuild := Some( url( s"https://github.com/taig/$id" ) )

licenses in ThisBuild := Seq( "MIT" -> url( s"https://raw.githubusercontent.com/taig/$id/master/LICENSE" ) )

organizationHomepage in ThisBuild := Some( url( "http://taig.io" ) )

pomExtra in ThisBuild := {
    <issueManagement>
        <url>https://github.com/taig/{id}/issues</url>
        <system>GitHub Issues</system>
    </issueManagement>
    <developers>
        <developer>
            <id>Taig</id>
            <name>Niklas Klein</name>
            <email>mail@taig.io</email>
            <url>http://taig.io/</url>
        </developer>
    </developers>
}

pomIncludeRepository in ThisBuild := { _ => false }

publishArtifact in ThisBuild in Test := false

publishMavenStyle in ThisBuild := true

publishTo in ThisBuild <<= version ( version => {
    val url = Some( "https://oss.sonatype.org/" )

    if( version.endsWith( "SNAPSHOT" ) ) {
        url.map( "snapshot" at _ + "content/repositories/snapshots" )
    }
    else {
        url.map( "release" at _ + "service/local/staging/deploy/maven2" )
    }
} )

scmInfo in ThisBuild := Some(
    ScmInfo(
        url( s"https://github.com/taig/$id" ),
        s"scm:git:git://github.com/taig/$id.git",
        Some( s"scm:git:git@github.com:taig/$id.git" )
    )
)

sonatypeProfileName in ThisBuild := "io.taig"

startYear in ThisBuild := Some( 2015 )