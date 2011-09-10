package org.ftw

case class HandledPaths[-A, +B, E <: BaseEnv](paths: Map[Vector[String], ResponderFactory[A, B, Responder[A, B] { type Env = E }]]) {
  private type Disappointment[A2, B2, E2] = Map[Vector[String], ResponderFactory[A2, B2, Responder[A2, B2] { type Env = E with E2 }]]
  
  def ~[A2 <: A, B2 >: B, E2 <: BaseEnv](hp: HandledPaths[A2, B2, E2]): HandledPaths[A2, B2, E with E2] =
    HandledPaths[A2, B2, E with E2]((paths ++ hp.paths).asInstanceOf[Disappointment[A2, B2, E2]])
}
