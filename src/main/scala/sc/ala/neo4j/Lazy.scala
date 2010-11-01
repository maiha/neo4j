package sc.ala.neo4j

case class Lazy[T](name:String) {
  class NotReady extends RuntimeException(name + " is not ready")
  var current:Option[T] = None
  def now:T = current.getOrElse(throw new NotReady)
  def <-- (value:T) = current = Some(value)
}
