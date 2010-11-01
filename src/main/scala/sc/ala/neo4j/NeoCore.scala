package sc.ala.neo4j

import org.neo4j.graphdb.DynamicRelationshipType
import org.neo4j.kernel.EmbeddedGraphDatabase
import org.neo4j.graphdb._

trait NeoCore { this:Neo =>
  var runningDB: Option[EmbeddedGraphDatabase] = None
  def db = runningDB match {
    case Some(value) => value
    case None => notConnected
  }
  def notConnected = throw new NotConnected
  def fileNotFound = throw new DatabaseNotFound(dir)
  def open  {
    if ((new java.io.File(dir)).isDirectory == false) fileNotFound
    runningDB = Some(new EmbeddedGraphDatabase(dir))
  }
  def close { db.shutdown; runningDB = None }

  def size =
    db.getConfig.getGraphDbModule.getNodeManager.getNumberOfIdsInUse(classOf[Node])

  def R[T](block: => T): T = {
    open
    try {
      block
    } finally {
      close
    }
  }

  def W[T](block: => T): T = {
    touch
    R[T]{
      val tx = db.beginTx	
      try {
	val res = block
	tx.success
	res
      } finally {
	tx.finish
      }
    }
  }
}

