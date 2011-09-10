package org.ftw

case class HttpResponse(contentType: String, os: Stream[Byte])
