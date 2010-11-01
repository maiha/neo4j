package sc.ala.neo4j

import org.neo4j.scala._
import sc.ala.neo4j.commands._

case class NotConnected() extends Throwable
case class DatabaseNotFound(msg:String) extends RuntimeException(msg)
case class NodeNotFound(msg:String) extends RuntimeException(msg)

class Neo(val dir:String) extends Neo4jWrapper
  with NeoCore with NeoAccessor
  with Example
  with Touch
  with Edges
  with Info
  with List
  with Dump
  with Load
  with BulkInsert
  with ShortestPath
  with DijkstraPath
  with Test

