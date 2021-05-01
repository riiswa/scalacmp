name := "scalacmp"

organization := "com.warisradji"
version := "0.1"
publishArtifact := true
publishMavenStyle := true

scalaVersion := "2.13.5"

libraryDependencies  ++= Seq(
  // Last stable release
  "org.scalanlp" %% "breeze" % "1.2",

  // The visualization library is distributed separately as well.
  // It depends on LGPL code
  "org.scalanlp" %% "breeze-viz" % "1.2",
)
