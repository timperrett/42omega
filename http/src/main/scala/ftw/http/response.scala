package ftw.http

case class HttpResponse(contentType: String, os: Stream[Byte])


