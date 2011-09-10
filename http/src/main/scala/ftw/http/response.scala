package ftw.http

import ftw._

case class HttpResponse(contentType: String, os: Stream[Byte]) extends Response
