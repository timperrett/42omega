package ftw.http

// case class HttpResponse(contentType: String, os: Stream[Byte])

case class HttpResponse(
  status: Status,
  headers: response.ResponseHeader,
  body: Stream[Byte],
  version: Version = Version.version11
)


