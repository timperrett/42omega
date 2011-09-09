package org.ftw

case class Path(parts: Vector[String]) {
  def /(path: String): Path =
    Path(parts :+ path)
  
  def handledBy[A, B <: Response, R <: Responder[A, B]](resp: ResponderFactory[A, B, R]): HandledPaths[A, B, R#Env] =
    new HandledPaths[A, B, R#Env](Map(parts -> resp.asInstanceOf[ResponderFactory[A, B, Responder[A, B] { type Env = R#Env }]]))
}

case class HandledPaths[A, B <: Response, E <: BaseEnv](paths: Map[Vector[String], ResponderFactory[A, B, Responder[A, B] { type Env = E }]]) {
  def ~[E2 <: BaseEnv](hp: HandledPaths[A, B, E2]): HandledPaths[A, B, E with E2] =
    HandledPaths[A, B, E with E2]((paths ++ hp.paths).asInstanceOf[Map[Vector[String], ResponderFactory[A, B, Responder[A, B] { type Env = E with E2 }]]])
}

