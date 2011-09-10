package org.ftw

case class HandledPaths[A, B <: Response, E <: BaseEnv](paths: Map[Vector[String], ResponderFactory[A, B, Responder[A, B] { type Env = E }]]) {
  def ~[E2 <: BaseEnv](hp: HandledPaths[A, B, E2]): HandledPaths[A, B, E with E2] =
    HandledPaths[A, B, E with E2]((paths ++ hp.paths).asInstanceOf[Map[Vector[String], ResponderFactory[A, B, Responder[A, B] { type Env = E with E2 }]]])
}
