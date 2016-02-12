resolvers += Resolver.sonatypeRepo( "snapshots" )

addSbtPlugin( "com.hanhuy.sbt" % "android-sdk-plugin" % "1.5.16" )

addSbtPlugin( "io.taig.sbt" % "scalariform" % "1.6.0" )

addSbtPlugin( "io.taig.sbt" % "sonatype" % "1.0.0-SNAPSHOT" )

addSbtPlugin( "org.scoverage" % "sbt-scoverage" % "1.3.5" )