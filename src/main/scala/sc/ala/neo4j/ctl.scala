package sc.ala.neo4j

object ctl {
  def main(args:Array[String]) {
    if (args.isEmpty) usage("Database not specified")
    val neo = new Neo(args head)
    runCommand(neo, args.tail.toList)
  }

  def runCommand(neo:Neo, args:List[String]) = try {
    args match {
      case Nil              => neo.info
      case "info"::Nil      => neo.info
      case "example"::Nil   => neo.example
      case "inspect"::Nil   => neo.info; neo.edges
      case "edges"::Nil     => neo.edges
      case "list"::Nil      => neo.list
      case "touch"::Nil     => neo.touch
      case "load"::tsv::Nil => neo.load(tsv)
      case "dump"::Nil      => neo.dump
      case "bulk"::tsv::Nil => neo.bulkInsert(tsv)
      case "test"::Nil      => neo.test
      case "path"::n1::n2::rel::Nil => neo.shortestPath(n1,n2,rel)
      case "dijkstra"::n1::n2::rel::Nil => neo.dijkstraPath(n1,n2,rel)
      case _ => usage("Invalid command")
    }
  } catch {
    case DatabaseNotFound(dir) => usage(format("Database not found: `%s'. Create it first like `touch' command", dir))
    case NodeNotFound(name) => usage(format("Node not found: `%s'", name))
  }

  def usage(mes:String) {
    scala.Console.err.println(mes)
    exit(-1)
  }
}

