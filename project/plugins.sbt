logLevel := Level.Warn
//
resolvers += Resolver.sonatypeRepo("releases")
//
//// Code formatting
//addSbtPlugin("com.geirsson" % "sbt-scalafmt" % "0.5.2-RC1")
//
//// Documenation
//addSbtPlugin("com.fortysevendeg" % "sbt-microsites" % "0.3.3")

addSbtPlugin("com.47deg" % "sbt-microsites" % "1.3.4")
//addSbtPlugin("com.eed3si9n" % "sbt-unidoc" % "0.3.3")
addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.6.3")

addSbtPlugin("com.github.sbt" % "sbt-unidoc" % "0.5.0")

//
//// Release
addSbtPlugin("com.github.sbt" % "sbt-release" % "1.1.0")
//addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "1.1")
addSbtPlugin("com.github.sbt" % "sbt-pgp" % "2.1.2")
//addSbtPlugin("ch.epfl.scala.index" % "sbt-scaladex" % "0.1.3")
//
//// Copyright headers
addSbtPlugin("de.heikoseeberger" % "sbt-header" % "5.6.0")
//
//addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.8.5")
