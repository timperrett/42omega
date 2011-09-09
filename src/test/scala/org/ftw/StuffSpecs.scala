package org.ftw

import org.specs2.mutable.Specification

object StuffSpecs extends Specification {
  
  val q = new Path[Unit, BaseEnv]
  val q2 = q ~> (new Resp1Factory) ~> (new Resp2Factory)
  
  assertType(q2).test[Path[Unit, SessionEnv with HttpEnv]]
  
  def assertType[A](a: A) = new {
    def test[B](implicit x: A =:= B) = x
  }
  
  trait SessionEnv extends BaseEnv
  trait HttpEnv extends BaseEnv
  
  class Resp1 extends Responder[Unit] {
    type Env = SessionEnv
    
    def render(env: Env)(u: Unit) = new Response {}
  }
  
  class Resp1Factory extends ResponderFactory[Unit, Resp1]
  
  class Resp2 extends Responder[Unit] {
    type Env = HttpEnv
    
    def render(env: Env)(u: Unit) = new Response {}
  }
  
  class Resp2Factory extends ResponderFactory[Unit, Resp2]
}
