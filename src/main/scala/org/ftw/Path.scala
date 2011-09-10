package org.ftw

case class Path(parts: Vector[String]) {
  def /(path: String): Path =
    Path(parts :+ path)
  
  def handledBy[A, B <: Response, R <: Responder[A, B]](resp: ResponderFactory[A, B, R]): HandledPaths[A, B, R#Env] =
    new HandledPaths[A, B, R#Env](Map(parts -> resp.asInstanceOf[ResponderFactory[A, B, Responder[A, B] { type Env = R#Env }]]))
}

