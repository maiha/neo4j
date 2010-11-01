package sc.ala.neo4j.commands
import sc.ala.neo4j.Neo

trait Example { this:Neo =>
  def example = W{
    nodes("Java") --> "-null"   --> nodes("Scala")
    nodes("Ruby") --> "+robust" --> nodes("Scala")
  }
  private val nodes = findNodeByName
}

