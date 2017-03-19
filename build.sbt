name := """ptbe"""

version := "1.0-SNAPSHOT"
lazy val raven = (project in file("modules/raven")).enablePlugins(PlayJava, PlayEbean, SbtTwirl)
lazy val root = (project in file(".")).enablePlugins(PlayJava,PlayEbean).dependsOn(raven).aggregate(raven)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)

libraryDependencies += evolutions
libraryDependencies += filters

// Specificy which are Ebeans, application.conf is not sufficient
playEbeanModels in Compile := Seq("models.ptbe.*")