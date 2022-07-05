configs(IntegrationTest)
inConfig(IntegrationTest)(Defaults.itSettings ++ Seq(IntegrationTest / fork := true))
