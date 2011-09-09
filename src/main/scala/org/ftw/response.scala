package org.ftw

trait Response {
  val os: Stream[Byte]
}

case class HttpResponse(contentType: String, os: Stream[Byte]) extends Response
