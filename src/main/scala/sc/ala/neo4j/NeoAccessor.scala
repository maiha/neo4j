package sc.ala.neo4j

import org.neo4j.graphdb.DynamicRelationshipType
import org.neo4j.kernel.EmbeddedGraphDatabase
import org.neo4j.graphdb._
import scala.collection.mutable.{HashMap,Map}
import scala.collection.JavaConversions._
import scala.Console.err

trait NeoAccessor { this:Neo =>
  def inspectNode(node:Node) = node("name") match {
    case Some(value) => format("%s:%s", node.getId, value)
    case None => node.getId.toString
  }

  val findNodeOrCreateByName = (name:String) =>
    namedNodes.getOrElseUpdate(name, newNodeNamed(name))
  val findNodeByName = (name:String) =>
    namedNodes.getOrElse(name, throw new NodeNotFound(name))

  private def newNodeNamed(name:String) = {
    val node = db.createNode
    node("name") = name
    node
  }

  lazy val namedNodes = {
    val hash = HashMap[String,Node]()
    val startedAt = System.nanoTime
    err.printf("loading current nodes...")
    db.getAllNodes foreach( node => {
      if (node.hasProperty("name")) {
	val name = node.getProperty("name").asInstanceOf[String]
	hash.update(name, node)
      }
    })
    val cachedSec = (System.nanoTime - startedAt)/1000000000.0
    err.println(format("done [%.1f]", cachedSec))
    hash
  }

  lazy val nodesName2id = {
    val hash = HashMap[String,Long]()
    db.getAllNodes foreach( node => {
      if (node.hasProperty("name")) {
	val name = node.getProperty("name").asInstanceOf[String]
	hash.update(name, node.getId)
      }
    })
    hash
  }

  val relations = Map[String, DynamicRelationshipType]()
  def types(name:String) = relations.getOrElseUpdate(
    name, DynamicRelationshipType.withName(name))
}

