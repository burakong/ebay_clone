name := """burak"""
organization := "com.burak"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.5"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies += "org.postgresql" % "postgresql" % "42.2.5"


libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "5.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0"
)

  herokuProcessTypes in Compile := Map(
  "web" -> "target/universal/stage/bin/play-app-test -Dhttp.port=$PORT"
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.burak.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.burak.binders._"
