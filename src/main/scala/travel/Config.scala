package travel

import com.typesafe.config.ConfigFactory

object Config {
  private val config = ConfigFactory.load()

  lazy val GraphqlUrl: String = config.getString("graphql.url")

  override def toString: String =
    s"""
      |CONFIG:
      | - GRAPHQL
      |   - url: $GraphqlUrl
      |""".stripMargin
}
