package sc.ala.neo4j.commands

import sc.ala.neo4j.{Neo,Tsv,Lazy}
import org.neo4j.kernel.EmbeddedGraphDatabase
import org.neo4j.graphdb.{Node,DynamicRelationshipType}
import scala.collection.mutable.{Map,HashMap}
import scala.collection.JavaConversions._
import scala.Console.err

// for bulk insert
import org.neo4j.kernel.impl.batchinsert.BatchInserter;
import org.neo4j.kernel.impl.batchinsert.BatchInserterImpl;

trait BulkInsert { this:Neo =>
  lazy val nodeCache = nodesName2id

  private class InvalidTriple(msg:String) extends RuntimeException(msg)
  private def batchConf = BatchInserterImpl.loadProperties("neo4j.props")
  private def newLoop { loopAt <-- System.nanoTime }
  private def endLoop {
    val sec = (System.nanoTime - loopAt.now)/1000000000.0
    val qps = if (sec>0) format("%.1f",numPerLoop/sec) else "---"
    err.println(format("(%09d)[%s]", count, qps))
    newLoop
  }

  private val numPerLoop = 100000
  private val loopAt     = Lazy[Long]("loopStartedAt")
  private val inserter   = Lazy[BatchInserterImpl]("inserter")
  private var count      = 0

  def bulkInsert(path:String) = {
    val tsv = new Tsv(path)
    W{ Unit }
    R{ nodeCache }
    inserter <-- new BatchInserterImpl(dir, batchConf)

    try {
      newLoop
      tsv.eachLine { line => {
	count += 1
	process(line)
	if (count % (numPerLoop/50) == 0) err.printf(".")
	if (count % numPerLoop == 0) endLoop
      } }
      endLoop
    } finally {
      inserter.now.shutdown
    }
  }

  private def newNodeNamed(name:String) = {
    val properties = Map[String,Object]()
    properties.put( "name", name )
    inserter.now.createNode( properties )
  }

  private def process(line:String) = {
    def nodes(name:String) = nodeCache.getOrElseUpdate( name, newNodeNamed(name))
    line match {
      case Tsv.Triple(n1,op,n2) =>
	if (n1 == n2) {
	  val msg = format("[IGNORE] same nodes! line:%d, data=%s",count, line)
	  err.println(msg)
	} else {
	  inserter.now.createRelationship( nodes(n1), nodes(n2), types(op), null)
	}
      case _ =>
	val msg = format("line:%d, data=%s", count, line)
        throw new InvalidInput(msg)
    }
  }
}

