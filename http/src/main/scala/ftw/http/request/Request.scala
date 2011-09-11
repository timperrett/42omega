package ftw.http.request

class Request(
  val line: Line, 
  val headers: List[(RequestHeader, String)], 
  _body: => Stream[Byte]){
    
    lazy val body: Stream[Byte] = _body
}

