package ftw

package object http {
  implicit def pathMatcher(s:String) = new PathMatcher(Vector(s))
  
  class PathMatcher(path:Vector[String]) extends Matcher[request.Request] {
    def / (part:String) = new PathMatcher(path :+ part)
    
    def matches(r:request.Request) = r.line.uri.parts == path
  }
}