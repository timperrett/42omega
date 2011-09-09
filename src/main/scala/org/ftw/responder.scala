package org.ftw

trait Responder[-A, +B <: Response] {
  type Env <: BaseEnv
  
  def render(env: Env)(req: A): B
}

trait ResponderFactory[-A, +B <: Response, +R <: Responder[A, B]] {
  def apply(): R
}
