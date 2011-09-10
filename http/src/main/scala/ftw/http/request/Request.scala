package ftw.http.request

case class Request(
  line: Line,
  headers: List[(RequestHeader, List[String])],
  body: Stream[Byte]
)

import ftw.http.Version

case class Line(
  method: Method,
  uri: Uri,
  version: Version
)

