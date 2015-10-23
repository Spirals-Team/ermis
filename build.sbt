name := "openstackApi"

version := "1.0"

scalaVersion := "2.11.7"

val akkaVersion = "2.3.7"

libraryDependencies ++= {
  val sprayV = "1.3.2"
  Seq(
    "io.spray"            %%  "spray-can"     % sprayV withSources() withJavadoc(),
    "io.spray"            %%  "spray-routing" % sprayV withSources() withJavadoc(),
    "io.spray"            %%  "spray-json"    % "1.3.1",
    "io.spray"            %%  "spray-testkit" % sprayV  % "test",
    "org.scalatest" % "scalatest_2.10" % "2.0" % "test",
    "com.typesafe.akka" % "akka-http-experimental_2.11" % "1.0",
    "org.reactivemongo" %% "reactivemongo" % "0.10.5.0.akka23",    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    //   "ch.qos.logback" % "logback-classic" % "1.1.2",
    "org.scala-lang.modules" % "scala-async_2.10" % "0.9.2",
    "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
    //    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    //    "org.slf4j" % "slf4j-simple" % "1.6.2",
    "org.slf4j" % "jul-to-slf4j" % "1.7.12",
    "org.slf4j" % "slf4j-log4j12" % "1.7.12",
    //   "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
    "com.typesafe.akka" %% "akka-agent" % akkaVersion,
    //"com.typesafe.akka" % "akka-stream-experimental_2.11" % "1.0-RC3",
    "com.typesafe.akka" % "akka-stream-experimental_2.11" % "1.0",
    "net.databinder.dispatch" %% "dispatch-json4s-native" % "0.11.2",
    "com.google.protobuf" % "protobuf-java" % "2.5.0",
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.4.2",
    "com.fasterxml.jackson.core" % "jackson-databind" % "2.2.2",
    "org.codehaus.jackson" % "jackson-mapper-asl" % "1.9.4",
    "com.woorea" %  "ceilometer-client"  % "3.2.1" withSources(),
    "com.woorea" %  "glance-client"      % "3.2.1" withSources(),
    "com.woorea" %  "keystone-client"    % "3.2.2-SNAPSHOT" withSources(),
    "com.woorea" %  "nova-client"        % "3.2.2-SNAPSHOT" withSources(),
    "com.woorea" %  "quantum-client"     % "3.2.1" withSources(),
    "com.woorea" %  "swift-client"       % "3.2.1" withSources(),
    "org.scalaj" % "scalaj-collection_2.10" % "1.6",
    "com.woorea" % "jersey2-connector" % "3.2.1"

  )
}
resolvers += Resolver.mavenLocal

logLevel in run := sbt.Level.Warn

javaOptions += "-Djava.util.logging.config.file=log4j.properties"
