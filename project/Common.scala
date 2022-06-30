import sbt._
import sbt.Keys._
object Common {

  organization in ThisBuild := "urbanmarkets.mandrill"

  organizationName in publish := "urbanmarkets"

  organization in publish := "urbanmarkets"

  scalaVersion in ThisBuild := "2.12.13"

  licenses := Seq("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.html"))

  scalacOptions in ThisBuild ++= Seq(
    "-target:jvm-1.8",
    "-deprecation",
    "-encoding",
    "UTF-8",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-language:experimental.macros",
    "-unchecked",
    "-Xfatal-warnings",
    "-Xlint",
    "-Ywarn-dead-code",
    "-Xfuture"
  )

}
