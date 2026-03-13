import xerial.sbt.Sonatype._

val scala3Version = "3.3.1"

lazy val root = project
  .in(file("."))
  .settings(
    credentials += Credentials(baseDirectory.value / "sonatype.credentials"),

    organization := "com.ip2location",
    name := "ip2location-scala",
    version := "8.5.0",

    scalaVersion := scala3Version,

    libraryDependencies += "org.scalameta" %% "munit" % "1.2.4" % Test,
    libraryDependencies += "com.google.code.gson" % "gson" % "2.13.2",

    // Publishing Config
    publishMavenStyle := true,
    sonatypeCredentialHost := sonatypeCentralHost,
    publishTo := sonatypePublishToBundle.value,

    usePgpKeyHex("9B922B12"),

    // Metadata
    licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT")),
    homepage := Some(url("https://github.com/ip2location/ip2location-scala")),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/ip2location/ip2location-scala"),
        "scm:git@github.com:ip2location/ip2location-scala.git"
      )
    ),
    developers := List(
      Developer(
        id = "ip2location",
        name = "IP2Location",
        email = "support@ip2location.com",
        url = url("https://www.ip2location.com")
      )
    )
  )