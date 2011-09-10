package org.ftw

trait Responder[-A, +B] {
  type Env <: BaseEnv
  
  def render(env: Env)(req: A): B
}

trait ResponderFactory[-A, +B, +R <: Responder[A, B]] {
  def apply(): R
}
