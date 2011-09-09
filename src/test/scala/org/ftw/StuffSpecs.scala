package org.ftw

import org.specs2.mutable.Specification

object StuffSpecs extends Specification {
  
  val a = Path(Vector("a")) handledBy (new Resp1Factory)
  val b = Path(Vector("b")) handledBy (new Resp2Factory)
  
  val ab = a ~ b
  
  assertType(ab).test[HandledPaths[Unit, SessionEnv with HttpEnv]]
  
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
