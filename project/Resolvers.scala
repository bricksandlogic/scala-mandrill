import sbt._

object Resolvers {

  val resolvers: Seq[sbt.librarymanagement.Resolver] = Seq(
    Resolver
      .url("sbt-plugin-release", url("http://dl.bintray.com/sbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns)
      .withAllowInsecureProtocol(true),
    Resolver.bintrayRepo("hseeberger", "maven"),
    Resolver.bintrayRepo("jarlakxen", "maven"),
    Resolver.bintrayRepo("mathieuancelin", "reactivecouchbase-maven"),
    Resolver.bintrayRepo("ovotech", "maven"),
    Resolver.bintrayRepo("commodityvectors", "commodityvectors-releases"),
    ("confluent" at "http://packages.confluent.io/maven").withAllowInsecureProtocol(true),
    ("lightshed-maven" at "http://dl.bintray.com/content/lightshed/maven").withAllowInsecureProtocol(true),
    ("maven2-repository.dev.java.net" at "http://download.java.net/maven/2").withAllowInsecureProtocol(true)
  )
}
