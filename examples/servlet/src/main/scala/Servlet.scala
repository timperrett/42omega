package ftw.examples.servlet

import ftw._, http._, servlet._, request._, response._

trait WhatIsItLike {
  def yeah = "Like a BOSS!!!"
}

trait WhoWantsIt {
  def meh = "We do!"
}

class LikeABoss extends OmegaFilter {

  def routingAndEnv =
    (("foo" handledBy Foo.factory) ~ ("bar" handledBy Bar.factory),
      new WhatIsItLike with WhoWantsIt {})

  object Foo extends Responder[Request, Response] {
    type Env = WhatIsItLike
    
    import ResponseHeader._
    
    def render(env: Env)(u: Request) =
      Response(OK, 
        List[(ResponseHeader, String)]((generalToResponse(Pragma) -> "no-cache"))
      , env.yeah.getBytes.toStream)
  }

  object Bar extends Responder[Request, Response] {
    type Env = WhoWantsIt

    def render(env: Env)(u: Request) =
      Response(OK, Nil, env.meh.getBytes.toStream)
  }

}
