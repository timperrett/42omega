package org.ftw

import org.specs2.mutable.Specification

object StuffSpecs extends Specification {
  
  val a = Path(Vector("a")) handledBy (new Resp1Factory)
  val b = Path(Vector("b")) handledBy (new Resp2Factory)
  
  val ab = a ~ b
  
  Runner.run(ab, new SessionEnv with HttpEnv {})
  
  assertType(ab).test[HandledPaths[Unit, HttpResponse, SessionEnv with HttpEnv]]
  
  def assertType[A](a: A) = new {
    def test[B](implicit x: A =:= B) = x
  }
  
  trait SessionEnv extends BaseEnv
  trait HttpEnv extends BaseEnv
  
  class Resp1 extends Responder[Unit, HttpResponse] {
    type Env = SessionEnv
    
    def render(env: Env)(u: Unit) = HttpResponse("text/plain", null)
  }
  
  class Resp1Factory extends ResponderFactory[Unit, HttpResponse, Resp1] {
    def apply() = new Resp1
  }
  
  class Resp2 extends Responder[Unit, HttpResponse] {
    type Env = HttpEnv
    
    def render(env: Env)(u: Unit) = HttpResponse("text/plain", null)
  }
  
  class Resp2Factory extends ResponderFactory[Unit, HttpResponse, Resp2] {
    def apply() = new Resp2
  }
}
