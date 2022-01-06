package travel

import com.typesafe.config.ConfigFactory

object Config {
  private val config = ConfigFactory.load()

  lazy val GraphqlUrl: String = config.getString("travel.graphqlUrl")
}
