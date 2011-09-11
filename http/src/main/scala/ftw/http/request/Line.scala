package ftw.http
package request

case class Line(
  method: Method,
  uri: Uri,
  version: Version
)