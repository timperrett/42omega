package ftw.http.request

class Request(
  val line: Line, 
  val headers: List[(RequestHeader, String)], 
  _body: => Stream[Byte]){
    
    lazy val body: Stream[Byte] = _body
}

import ftw.http.Version

case class Line(
  method: Method,
  uri: Uri,
  version: Version
)

