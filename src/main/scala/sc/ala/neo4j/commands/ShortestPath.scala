package sc.ala.neo4j.commands

import sc.ala.neo4j._
import org.neo4j.kernel.EmbeddedGraphDatabase
import org.neo4j.graphdb.{Node,DynamicRelationshipType,Direction}
import org.neo4j.graphalgo.impl.shortestpath._
import scala.collection.mutable.Map
import scala.collection.JavaConversions._
import scala.Console.err

trait ShortestPath { this:Neo =>
  private def nodeNamed(name:String) = namedNodes.getOrElse(name, throw new NodeNotFound(name))

  def shortestPath(n1:String, n2:String, relName:String) = R{
    val rel   = DynamicRelationshipType.withName(relName)
    val path  = new SingleSourceShortestPathBFS(n(n1), Direction.BOTH, rel)
    val nodes = path.getPathAsNodes(n(n2))

    nodes foreach( n => {
      printf("%s\n", inspectNode(n))
    })
  }
}
