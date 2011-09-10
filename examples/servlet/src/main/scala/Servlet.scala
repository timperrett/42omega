package ftw.examples.servlet

import ftw._

class LikeABoss extends OmegaFilter {
  def routingAndEnv = 
    (Path(Vector("foo")) handledBy FooFactory, new BaseEnv {})
  
  object FooFactory extends ResponderFactory[Unit, HttpResponse, Foo.type] {
    def apply() = Foo
  }
  
  object Foo extends Responder[Unit, HttpResponse] {
    type Env = BaseEnv
    
    def render(env: Env)(u: Unit) =
      HttpResponse("text/plain", "Like a boss!".getBytes.toStream)
  }
}
