name := """easylancer-core"""
organization := "com.easylancer"
maintainer := "ahmed@easylancer.com"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  guice,
  ws,
  ehcache,
  filters,
  "org.mongodb" % "mongo-java-driver" % "3.0.1",
)