package sc.ala.neo4j.commands

import sc.ala.neo4j.Neo
import org.neo4j.kernel.EmbeddedGraphDatabase

trait Touch { this:Neo =>
  def touch {
    (new EmbeddedGraphDatabase(dir)).shutdown
  }
}
