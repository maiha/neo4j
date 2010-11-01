package sc.ala.neo4j

object Tsv {
  class Triple(a:String, b:String, c:String)
  object Triple {
    val delimiter = "\t"
    def unapply(str: String): Option[(String,String,String)] = {
      val parts = str.split(delimiter)
      if (parts.size == 3) Some(parts(0), parts(1), parts(2)) else None
    }
  }
}

class Tsv(path:String) {
  import java.io._

  def W(block: BufferedWriter => Unit) {
    val fos    = new FileOutputStream(path)
    var writer = new BufferedWriter(new OutputStreamWriter(fos))

    try {
      block(writer)
    } finally {
      writer.close
      fos.close
    }
  }

  def eachLine(block: String => Unit) {
    var done        = false
    var line:String = null

    val fis    = new FileInputStream(path)
    val reader = new BufferedReader(new InputStreamReader(fis))

    try {
      while (!done) {
        line = reader.readLine()

        if (line == null)
          done = true
        else
	  block(line)
      }
    } finally {
      reader.close
      fis.close
    }
  }
} 

