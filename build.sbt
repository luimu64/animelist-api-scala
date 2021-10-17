val ScalatraVersion = "2.8.2"

ThisBuild / scalaVersion := "2.13.4"
ThisBuild / organization := "luimu.dev"

lazy val hello = (project in file("."))
  .settings(
    name := "animelist-api",
    version := "0.1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      "com.github.jwt-scala" %% "jwt-play-json" % "9.0.2",
      "mysql" % "mysql-connector-java" % "8.0.25",
      "org.scalatra" %% "scalatra" % ScalatraVersion,
      "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
      "com.typesafe.play" %% "play-json" % "2.9.2",
      "ch.qos.logback" % "logback-classic" % "1.2.6" % "runtime",
      "org.eclipse.jetty" % "jetty-webapp" % "11.0.6" % "container",
      "javax.servlet" % "javax.servlet-api" % "4.0.1" % "provided"
    )
  )

enablePlugins(SbtTwirl)
enablePlugins(JettyPlugin)
