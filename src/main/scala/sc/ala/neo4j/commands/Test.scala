package sc.ala.neo4j.commands

import sc.ala.neo4j.Neo
import scala.collection.JavaConversions._

trait Test { this:Neo =>
  def test = W{
    printf("[%s]\n", "OK")
  }
}

