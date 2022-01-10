package travel

import com.typesafe.config.ConfigFactory

object Config {
  private val config = ConfigFactory.load()

  lazy val GraphqlUrl: String = config.getString("graphql.url")
  lazy val port: Int = config.getInt("travel.port")

  override def toString: String =
    s"""
      |CONFIG:
      | - TRAVEL
      |   - port: $port
      | - GRAPHQL
      |   - url: $GraphqlUrl
      |""".stripMargin
}
