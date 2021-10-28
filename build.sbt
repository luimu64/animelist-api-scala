val ScalatraVersion = "2.8.2"

ThisBuild / scalaVersion := "2.13.4"
ThisBuild / organization := "luimu.dev"

resolvers ++= Seq("jBCrypt Repository" at "https://repo1.maven.org/maven2/org/")

lazy val hello = (project in file("."))
  .settings(
    name := "animelist-api",
    version := "1.0",
    libraryDependencies ++= Seq(
      "com.github.t3hnar" %% "scala-bcrypt" % "4.3.0",
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
