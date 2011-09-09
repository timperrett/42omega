package org.ftw

import java.io.OutputStream

trait BaseEnv

trait Responder[-A] {
  type Env <: BaseEnv
  
  def render(env: Env)(req: A): Response
}

trait ResponderFactory[-A, R <: Responder[A]] {
}

class Path[A, E <: BaseEnv] {
  def ~>[R <: Responder[A]](resp: ResponderFactory[A, R]): Path[A, E with R#Env] =
    new Path[A, E with R#Env]
}

trait Response

case class StreamResponse(mime: String, os: OutputStream) extends Response
