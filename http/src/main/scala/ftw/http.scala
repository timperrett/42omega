package ftw

package object http {
  implicit def pathMatcher(s:String) = new PathRoute(Vector(s))
  
  class PathRoute(path:Vector[String]) extends Route[request.Request, Vector[String]] {
    def / (part:String) = new PathRoute(path :+ part)
    
    def unapply(r:request.Request) = if(r.line.uri.parts == path) Some(path) else None
  }
}