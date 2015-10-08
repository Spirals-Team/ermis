<<<<<<< HEAD
name := "ermis"
=======
name := "openstackApi"
>>>>>>> master

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
<<<<<<< HEAD
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-agent" % akkaVersion,
    "com.typesafe.akka" % "akka-stream-experimental_2.11" % "1.0-M4",
    "net.databinder.dispatch" %% "dispatch-json4s-native" % "0.11.2",
    "com.google.protobuf" % "protobuf-java" % "2.5.0",
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.4.2",
    "com.woorea" %  "ceilometer-client"  % "3.1.1" withSources(),
    "com.woorea" %  "glance-client"      % "3.1.1" withSources(),
    "com.woorea" %  "keystone-client"    % "3.1.1" withSources(),
    "com.woorea" %  "nova-client"        % "3.1.1" withSources(),
    "com.woorea" %  "quantum-client"     % "3.1.1" withSources(),
    "com.woorea" %  "swift-client"       % "3.1.1" withSources(),
    "com.woorea" % "jersey2-connector" % "3.1.1",
    "com.woorea" %  "openstack-examples" % "3.1.1" withSources()
  )

}
=======
    "com.typesafe.akka" % "akka-http-experimental_2.11" % "1.0-RC3",
    "org.reactivemongo" %% "reactivemongo" % "0.10.5.0.akka23",    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "ch.qos.logback" % "logback-classic" % "1.1.2",
    "org.scala-lang.modules" % "scala-async_2.10" % "0.9.2",
    "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-agent" % akkaVersion,
    "com.typesafe.akka" % "akka-stream-experimental_2.11" % "1.0-RC3",
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
    "com.woorea" % "jersey2-connector" % "3.2.2-SNAPSHOT"
//    "com.woorea" %  "openstack-examples" % "3.1.1" withSources()

  )
}
resolvers += Resolver.mavenLocal
>>>>>>> master
