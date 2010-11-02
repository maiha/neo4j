package sc.ala.neo4j.commands

import sc.ala.neo4j.{Neo,Tsv}
import scala.collection.JavaConversions._
import scala.Console.err

trait Load { this:Neo =>
  class InvalidInput(msg:String) extends RuntimeException(msg)

  def load(path:String) = W{
    val tsv = new Tsv(path)
    count = 0
    tsv.eachLine { line => {
      count += 1
      lines += line
      if (count % (numPerLoop/50) == 0) printf(".")
      if (count % numPerLoop == 0) flush
    } }
    flush
  }

  private var count = 0
  private var lines = scala.collection.mutable.ArrayBuffer[String]()
  private val numPerLoop = 5000

  private def flush {
    if (!lines.isEmpty) {
      val tx = db.beginTx
      try {
	var lineno = count - lines.size
	lines foreach( line => {
	  lineno += 1
	  val a = line.split("\t")
	  if (a.size == 3) {
	    if (a(0) == a(2)) {
	      val msg = format("[IGNORE] same nodes! line:%d, data=%s",lineno, line)
	      err.println(msg)
	    } else {
	      N(a(0)) --> a(1) --> N(a(2))
	    }
	  } else {
	    val msg = format("line:%d, size=%d, data=%s",lineno, a.size, line)
	    throw new InvalidInput(msg)
	  }
	})
	tx.success
	lines.clear
	printf("(%09d)\n", count)
      } finally {
	tx.finish
      }
    }
  }
}

