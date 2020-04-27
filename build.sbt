
name := "trello-clone-server"

version := "0.1"

scalaVersion := "2.13.0"

val circeVersion = "0.12.3"

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    Defaults.itSettings,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"   % "10.1.11",
      "com.typesafe.akka" %% "akka-stream" % "2.5.26", // or whatever the latest version is
      "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.11",
      "de.heikoseeberger" %% "akka-http-circe" % "1.31.0",
      "com.typesafe.akka" %% "akka-actor-typed" % "2.6.3",
      "ch.megard" %% "akka-http-cors" % "0.4.3",
      "org.scalatest" %% "scalatest" % "3.1.1" % "it,test",
      "org.typelevel" %% "cats-core" % "2.0.0",
      "io.circe" %% "circe-core" % circeVersion,
      "io.circe" %% "circe-generic" % circeVersion,
      "io.circe" %% "circe-parser" % circeVersion,
      "com.softwaremill.macwire" %% "macros" % "2.3.3" % "provided",
      "com.softwaremill.macwire" %% "macrosakka" % "2.3.3" % "provided",
      "com.softwaremill.macwire" %% "proxy" % "2.3.3",
      "org.scalikejdbc" %% "scalikejdbc"       % "3.4.1",
      "org.scalikejdbc" %% "scalikejdbc-config"  % "3.4.1",
      "org.scalikejdbc" %% "scalikejdbc-syntax-support-macro" % "3.4.1",
      "com.h2database"  %  "h2"                % "1.4.200",
      "ch.qos.logback"  %  "logback-classic"   % "1.2.3",
    ),
  )

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

dockerBaseImage := "trello-server-base:1.0"
dockerExposedPorts in Docker := Seq(9000, 5005) //9000:アプリケーション, 5005:デバッグ
