package sc.ala.neo4j.commands

import sc.ala.neo4j.Neo
import org.neo4j.graphdb.Node
import scala.collection.JavaConversions._

trait Edges { this:Neo =>
  def edges = R{
    for (n1 <- db.getAllNodes) {
      for (r <- n1.getRelationships) {
	val n2 = r.getEndNode
	if (n1 != n2)
	  printf("[%s] --(%s)--> [%s]\n",
		 inspectNode(n1), r.getType, inspectNode(n2))
      }
    }
  }
}

