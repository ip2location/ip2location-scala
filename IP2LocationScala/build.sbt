ThisBuild / version := "8.4.0"

ThisBuild / scalaVersion := "3.3.4"

lazy val root = (project in file("."))
  .settings(
    name := "IP2LocationScala"
  )

libraryDependencies ++= Seq(
  "com.google.code.gson" % "gson" % "2.13.1",
  "org.scalatest" %% "scalatest" % "3.2.19" % Test
  // other deps
)