package sc.ala.neo4j.commands
import sc.ala.neo4j.Neo

trait Example { this:Neo =>
  def example = W{
    val java  = nodes("Java")
    val ruby  = nodes("Ruby")
    val scala = nodes("Scala")
    java --> "-null" --> scala <-- "+robust" <-- ruby
  }
  private val nodes = findNodeOrCreateByName
}

