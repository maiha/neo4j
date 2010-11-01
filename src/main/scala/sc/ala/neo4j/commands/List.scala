package sc.ala.neo4j.commands

import sc.ala.neo4j.Neo
import scala.collection.JavaConversions._

trait List { this:Neo =>
  def list = R{db.getAllNodes foreach(node => println(node.getId))}
}
