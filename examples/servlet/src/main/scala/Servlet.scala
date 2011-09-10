package ftw.examples.servlet

import ftw._, http._, servlet._

trait WhatIsItLike {
  def yeah:String = "Like a BOSS!!!"
}

class LikeABoss extends OmegaFilter {
  def routingAndEnv = 
    (Path(Vector("foo")) handledBy FooFactory, new WhatIsItLike {})
  
  object FooFactory extends ResponderFactory[Unit, HttpResponse, Foo.type] {
    def apply() = Foo
  }
  
  object Foo extends Responder[Unit, HttpResponse] {
    type Env = WhatIsItLike
    
    def render(env: Env)(u: Unit) =
      HttpResponse("text/plain", env.yeah.getBytes.toStream)
  }
}
