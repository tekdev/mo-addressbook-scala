name := "addressbook"

version := "1.0"

scalaVersion := "2.12.1"


libraryDependencies ++= {
  Seq(
    "com.typesafe" % "config" % "1.3.1",
    "ch.qos.logback" % "logback-classic" % "1.1.7",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
    "org.scalatest" %% "scalatest" % "3.0.1" % "test",
    "org.mockito" % "mockito-all" % "1.10.19" % "test"
  )
}