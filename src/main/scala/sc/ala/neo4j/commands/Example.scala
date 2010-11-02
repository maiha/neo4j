package sc.ala.neo4j.commands
import sc.ala.neo4j.Neo

trait Example { this:Neo =>
  def example = W{
    N("Java") --> "-null" --> N("Scala") <-- "+robust" <-- N("Ruby")
  }
}

