package sc.ala.neo4j.commands

import sc.ala.neo4j.Neo
import org.neo4j.graphdb.Node

trait Info { this:Neo =>
  def info = printf("nodes: %s\n", R(nodeSize))

  private def nodeSize =
    db.getConfig.getGraphDbModule.getNodeManager.getNumberOfIdsInUse(classOf[Node])
}
