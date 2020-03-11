name := "jivox-assignment"

version := "0.1"

scalaVersion := "2.12.8"

val akkaVersion = "2.6.3"
val akkaHttpVersion = "10.1.7"
val scalaTestVersion = "3.0.5"

libraryDependencies ++= Seq(
  // akka
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-remote" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence" % "2.6.3",
  // akka http
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion,
  // testing
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "org.scalatest" %% "scalatest" % scalaTestVersion,
  
  // JWT
  "com.pauldijou" %% "jwt-spray-json" % "2.1.0",
  "io.aeron" % "aeron-driver" % "1.25.1",
  "io.aeron" % "aeron-client" % "1.25.1",
  "org.iq80.leveldb"            % "leveldb"          % "0.7",
"org.fusesource.leveldbjni"   % "leveldbjni-all"   % "1.8"

)