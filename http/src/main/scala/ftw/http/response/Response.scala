package ftw.http.response

import ftw.http._

case class HttpResponse(
  status: Status,
  headers: List[(ResponseHeader, String)],
  body: Stream[Byte],
  version: Version = Version.version11
)
