name := "AkkaBigPrimesScala"

version := "0.1"

scalaVersion := "2.13.3"

val akkaVersion = "2.6.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka"        %% "akka-actor-typed"         % akkaVersion,
  "com.typesafe.akka"        %% "akka-persistence-typed"   % akkaVersion,
  "com.typesafe.akka"        %% "akka-actor-testkit-typed" % akkaVersion % Test,
  "org.scalatest"            %% "scalatest"                % "3.0.8" % Test
)
