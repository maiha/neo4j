package sc.ala.neo4j.commands

import sc.ala.neo4j._
import org.neo4j.kernel.EmbeddedGraphDatabase
import org.neo4j.graphdb._
import org.neo4j.graphalgo.GraphAlgoFactory
import org.neo4j.kernel.Traversal
import scala.collection.mutable.Map
import scala.collection.JavaConversions._
import scala.Console.err

trait DijkstraPath { this:Neo =>
  private val nodes = findNodeByName
  def dijkstraPath(n1:String, n2:String, relName:String) = R{
    val rel   = DynamicRelationshipType.withName(relName)
    val trav  = Traversal.expanderForTypes( rel, Direction.BOTH )
    val algo  = GraphAlgoFactory.shortestPath(trav, 15)
    val paths = algo.findAllPaths(nodes(n1), nodes(n2))

    println("size="+paths.size)
    // TODO: this raises null pointer exception
    paths foreach(println)
  }
}
