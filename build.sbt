name := "jivox-assignment"

version := "0.1"

scalaVersion := "2.13.1"

val akkaVersion = "2.6.3"
val akkaHttpVersion = "10.1.11"
val scalaTestVersion = "3.1.1"
val sprayJsonVersion = "4.3.0"

libraryDependencies ++= Seq(
  // akka
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence-query" % akkaVersion,
  // akka http
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion,
  // testing
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "org.scalatest" %% "scalatest" % scalaTestVersion,
  
  // JWT
  "com.pauldijou" %% "jwt-spray-json" % sprayJsonVersion,
  "com.typesafe.akka" %% "akka-slf4j" % "2.6.3",
  "ch.qos.logback" % "logback-classic" % "1.2.3",

  "com.typesafe.akka" %% "akka-persistence-cassandra" % "0.103" ,
  "com.typesafe.akka" %% "akka-persistence-cassandra-launcher" % "0.103" % Test,
"io.aeron" % "aeron-driver" % "1.25.1",
"io.aeron" % "aeron-client" % "1.25.1"


)