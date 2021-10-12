val ScalatraVersion = "2.8.2"

ThisBuild / scalaVersion := "2.13.4"
ThisBuild / organization := "luimu.dev"

lazy val hello = (project in file("."))
  .settings(
    name := "animelist-api",
    version := "0.1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      "mysql" % "mysql-connector-java" % "5.1.+",
      "org.scalatra" %% "scalatra" % ScalatraVersion,
      "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
      "com.typesafe.play" %% "play-json" % "2.9.2",
      "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
      "org.eclipse.jetty" % "jetty-webapp" % "9.4.35.v20201120" % "container",
      "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided"
    )
  )

enablePlugins(SbtTwirl)
enablePlugins(JettyPlugin)
