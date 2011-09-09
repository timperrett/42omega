package org.ftw

case class Path(parts: Vector[String]) {
  def /(path: String): Path =
    Path(parts :+ path)
  
  def handledBy[A, R <: Responder[A]](resp: ResponderFactory[A, R]): HandledPaths[A, R#Env] =
    new HandledPaths[A, R#Env](Map(parts -> resp))
}

case class HandledPaths[A, E <: BaseEnv](paths: Map[Vector[String], ResponderFactory[A, Responder[A]]]) {
  def ~[E2 <: BaseEnv](hp: HandledPaths[A, E2]): HandledPaths[A, E with E2] =
    HandledPaths[A, E with E2](paths ++ hp.paths)
}

