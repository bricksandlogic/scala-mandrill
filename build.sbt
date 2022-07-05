//import com.typesafe.sbt.SbtGhPages.GhPagesKeys._
import sbtunidoc.ScalaUnidocPlugin._
import ReleaseTransformations._
import de.heikoseeberger.sbtheader.License.ALv2
import de.heikoseeberger.sbtheader.License._
import Common.supportedScalaVersions

resolvers := Resolvers.resolvers

headerLicenseStyle := HeaderLicenseStyle.SpdxSyntax

lazy val commonSettings = Seq(
  // License headers
  headerLicense := Some(HeaderLicense.ALv2("2015", "Heiko Seeberger"))
)

lazy val credentialSettings = Seq(
  credentials += Credentials(
    "Sonatype Nexus Repository Manager",
    "nexus.internal.urban-markets.co.uk",
    "admin",
    """~mn&~=:~FfGDln6Q]W"B[^KX)mw#{LCHeUd%nk1BAum~N5?rELDwO;B>`[w5IXzj"""
  )
  // For Travis CI - see http://www.cakesolutions.net/teamblogs/publishing-artefacts-to-oss-sonatype-nexus-using-sbt-and-travis-ci
//  credentials ++= (for {
//    username <- Option(System.getenv().get("SONATYPE_USERNAME"))
//    password <- Option(System.getenv().get("SONATYPE_PASSWORD"))
//  } yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password)).toSeq
)
//
lazy val sharedPublishSettings = Seq(
  crossScalaVersions := supportedScalaVersions,
  releaseCrossBuild := true,
  releaseTagName := s"v${if (releaseUseGlobalVersion.value) (version in ThisBuild).value else version.value}",
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := Function.const(false),
  organization := "urbanmarkets",
  organizationName := "urbanmarkets",
  publishTo := {
    val nexus = "https://nexus.internal.urban-markets.co.uk/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "repository/maven-snapshots/")
    else
      Some("releases" at nexus + "repository/maven-releases/")
  }
)

lazy val sharedReleaseProcess = Seq(
  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    publishArtifacts,
    //releaseStepTask(publish in Scaladex),
    setNextVersion,
    commitNextVersion,
    pushChanges,
    releaseStepCommand("sonatypeRelease")
  )
)
//)
lazy val publishSettings = Seq(
  homepage := Some(url("https://github.com/gutefrage/scala-mandrill")),
  licenses := Seq("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.html")),
  scmInfo := Some(
    ScmInfo(url("https://www.apache.org/licenses/LICENSE-2.0.html"),
            "scm:git:git@github.com:gutefrage/scala-mandrill.git")),
  autoAPIMappings := true,
  apiURL := Some(url("https://gutefrage.github.io/scala-mandrill/api")),
  pomExtra := (
    <developers>
      <developer>
        <id>muuki88</id>
        <name>Nepomuk Seiler</name>
        <url>https://github.com/muuki88/</url>
      </developer>
      <developer>
        <id>lunaryorn</id>
        <name>Sebastian Wiesner</name>
        <url>http://www.lunaryorn.com</url>
      </developer>
    </developers>
  )
  // Scaladex publishing
) ++ credentialSettings ++ sharedPublishSettings ++ sharedReleaseProcess
//
lazy val core = project
  .in(file("core"))
  .enablePlugins(AutomateHeaderPlugin)
  .settings(commonSettings)
  .settings(publishSettings)
  .settings(
    name := "mandrill-core",
    libraryDependencies ++= Seq(
      ("com.beachape" %% "enumeratum" % "1.6.1").cross(CrossVersion.for3Use2_13),
//      ("com.beachape" %% "enumeratum-circe" % "1.6.1").cross(CrossVersion.for3Use2_13)
//      "joda-time" % "joda-time" % "2.9.6",
//      "org.joda" % "joda-convert" % "1.8.1",
//      "com.chuusai" %% "shapeless" % "2.3.2", // Generic programming for scala
//      "org.julienrf" %% "enum" % "3.0" // Enum utilities for ADTs
    )
  )

lazy val testkit = project
  .in(file("test-kit"))
  .enablePlugins(AutomateHeaderPlugin)
  .settings(commonSettings)
  .settings(publishSettings)
  .settings(
    name := "mandrill-test-kit",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.12",
      "org.scalacheck" %% "scalacheck" % "1.16.0"
    )
  )
  .dependsOn(core)
//
lazy val playjson = project
  .in(file("play-json"))
  .enablePlugins(AutomateHeaderPlugin)
  .settings(commonSettings)
  .settings(publishSettings)
  .settings(
    name := "mandrill-play-json",
    libraryDependencies ++= Seq(
      "com.typesafe.play" %% "play" % "2.8.15",
      "com.typesafe.play" %% "play-json" % "2.9.2",
      "com.chuusai" %% "shapeless" % "2.4.0-M1"
    )
  )
  .dependsOn(core, testkit % "test->compile")

lazy val mandrillcirce = project
  .in(file("mandrill-circe"))
  .enablePlugins(AutomateHeaderPlugin)
  .settings(commonSettings)
  .settings(publishSettings)
  .settings(
    name := "mandrill-circe",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
    ).map(_ % "0.14.2")
  )
  .dependsOn(core, testkit % "test->compile")

lazy val http4s = project
  .in(file("mandrill-http4s"))
  .enablePlugins(AutomateHeaderPlugin)
  .settings(commonSettings)
  .settings(publishSettings)
  .settings(
    name := "mandrill-http4s",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-dsl",
      "org.http4s" %% "http4s-ember-client",
      "org.http4s" %% "http4s-circe",
    ).map(_ % "0.23.12"),
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.12" % "it"
  )
  .dependsOn(core, mandrillcirce)

lazy val noPublishSettings = Seq(
  publish := (),
  publishLocal := (),
  publishArtifact := false
)

lazy val scala_mandrill =
  project
    .in(file("."))
    .settings(
      moduleName := "root",
      crossScalaVersions := Nil,
    )
    .settings(publishSettings)
    .settings(noPublishSettings)
    .aggregate(core, testkit, mandrillcirce, http4s)

//lazy val docs = project
//  .in(file("docs"))
//  .enablePlugins(MicrositesPlugin)
//  .settings(noPublishSettings)
//  .settings(unidocSettings)
//  .settings(ghpages.settings)
//  .settings(
//    micrositeName := "Scala Mandrill",
//    micrositeDescription := "Minimal Scala API for Mandrill",
//    micrositeAuthor := "Gutefrage.net GmbH",
//    micrositeHomepage := "https://www.gutefrage.net",
//    micrositeGithubOwner := "gutefrage",
//    micrositeGithubRepo := "scala-mandrill",
//    micrositeBaseUrl := "/scala-mandrill",
//    micrositeDocumentationUrl := "docs",
//    micrositeTwitter := "gutefrage_net",
//    micrositeHighlightTheme := "atom-one-light",
//    micrositeExtraMdFiles := Map(file("README.md") -> "readme.md"),
//    micrositePalette := Map(
//      // primary and secondary color are swapped
//      "brand-primary" -> "#4DB8AF",
//      "brand-secondary" -> "#49A0CC",
//      "brand-tertiary" -> "#222749",
//      "gray-dark" -> "#646767",
//      "gray" -> "#A8ACAD",
//      "gray-light" -> "#CACDCD",
//      "gray-lighter" -> "#EDEEEE",
//      "white-color" -> "#FFFFFF"
//    ),
//    git.remoteRepo := "git@github.com:gutefrage/scala-mandrill.git",
//    autoAPIMappings := true,
//    docsMappingsAPIDir := "api",
//    addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), docsMappingsAPIDir),
//    ghpagesNoJekyll := false,
//    fork in tut := true,
//    fork in (ScalaUnidoc, unidoc) := true,
//    includeFilter in makeSite := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.yml" | "*.md",
//    // dependencies for documentation examples
//    libraryDependencies ++= Seq(
//      "com.typesafe.play" %% "play-ws" % "2.5.10"
//    )
//  )
//  .dependsOn(core, playjson, testkit)
////
//************************************************************************************************//
//lazy val core = project
//  .in(file("core"))
//  .enablePlugins(AutomateHeaderPlugin)
//  .settings(commonSettings)
//  .settings(publishSettings)
//  .settings(
//    name := "mandrill-core",
//    libraryDependencies ++= Seq(
//      "joda-time" % "joda-time" % "2.9.6",
//      "org.joda" % "joda-convert" % "1.8.1",
//      "com.chuusai" %% "shapeless" % "2.3.2", // Generic programming for scala
//      "org.julienrf" %% "enum" % "3.0" // Enum utilities for ADTs
//    )
//  )
//
//lazy val testkit = project
//  .in(file("test-kit"))
//  .enablePlugins(AutomateHeaderPlugin)
//  .settings(commonSettings)
//  .settings(publishSettings)
//  .settings(
//    name := "mandrill-test-kit",
//    libraryDependencies ++= Seq(
//      "org.scalatest" %% "scalatest" % "3.0.0",
//      "org.scalacheck" %% "scalacheck" % "1.13.4"
//    )
//  )
//  .dependsOn(core)
//
//lazy val playjson = project
//  .in(file("play-json"))
//  .enablePlugins(AutomateHeaderPlugin)
//  .settings(commonSettings)
//  .settings(publishSettings)
//  .settings(
//    name := "mandrill-play-json",
//    libraryDependencies ++= Seq(
//      "com.typesafe.play" %% "play-json" % "2.5.10"
//    )
//  )
//  .dependsOn(core, testkit % "test->compile")
//
//lazy val docsMappingsAPIDir = settingKey[String]("Name of subdirectory in site target directory for api docs")
//

//lazy val commonSettings = Seq(
//  // License headers
//  headers := createFrom(Apache2_0, "2016-2017", "gutefrage.net GmbH")
//)

//lazy val noPublishSettings = Seq(
//  publish := (),
//  publishLocal := (),
//  publishArtifact := false
//)
//
//lazy val publishSettings = Seq(
//    homepage := Some(url("https://github.com/gutefrage/scala-mandrill")),
//    licenses := Seq("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.html")),
//    scmInfo := Some(
//      ScmInfo(url("https://www.apache.org/licenses/LICENSE-2.0.html"),
//              "scm:git:git@github.com:gutefrage/scala-mandrill.git")),
//    autoAPIMappings := true,
//    apiURL := Some(url("https://gutefrage.github.io/scala-mandrill/api")),
//    pomExtra := (
//      <developers>
//      <developer>
//        <id>muuki88</id>
//        <name>Nepomuk Seiler</name>
//        <url>https://github.com/muuki88/</url>
//      </developer>
//      <developer>
//        <id>lunaryorn</id>
//        <name>Sebastian Wiesner</name>
//        <url>http://www.lunaryorn.com</url>
//      </developer>
//    </developers>
//    ),
//    // Scaladex publishing
//    scaladexKeywords in Scaladex := Seq("mandrill", "email")
//  ) ++ credentialSettings ++ sharedPublishSettings ++ sharedReleaseProcess
//
//lazy val credentialSettings = Seq(
//  // For Travis CI - see http://www.cakesolutions.net/teamblogs/publishing-artefacts-to-oss-sonatype-nexus-using-sbt-and-travis-ci
//  credentials ++= (for {
//    username <- Option(System.getenv().get("SONATYPE_USERNAME"))
//    password <- Option(System.getenv().get("SONATYPE_PASSWORD"))
//  } yield Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", username, password)).toSeq
//)
//
//lazy val sharedPublishSettings = Seq(
//  releaseCrossBuild := true,
//  releaseTagName := s"v${if (releaseUseGlobalVersion.value) (version in ThisBuild).value else version.value}",
//  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
//  publishMavenStyle := true,
//  publishArtifact in Test := false,
//  pomIncludeRepository := Function.const(false),
//  publishTo := {
//    val nexus = "https://oss.sonatype.org/"
//    if (isSnapshot.value)
//      Some("Snapshots" at nexus + "content/repositories/snapshots")
//    else
//      Some("Releases" at nexus + "service/local/staging/deploy/maven2")
//  }
//)
//
//lazy val sharedReleaseProcess = Seq(
//  releaseProcess := Seq[ReleaseStep](
//    checkSnapshotDependencies,
//    inquireVersions,
//    runClean,
//    runTest,
//    setReleaseVersion,
//    commitReleaseVersion,
//    tagRelease,
//    publishArtifacts,
//    releaseStepTask(publish in Scaladex),
//    setNextVersion,
//    commitNextVersion,
//    pushChanges,
//    releaseStepCommand("sonatypeRelease")
//  )
//)
