package ftw.examples.servlet

import ftw._, http._, servlet._, request._, response._

trait Base

trait WhatIsItLike {
  def yeah = "Like a BOSS!!!"
}

trait WhoWantsIt {
  def meh = "We do!"
}

class LikeABoss extends OmegaFilter {

  def routingAndEnv =
    (("foo" handledBy Foo.factory) ~
     ("bar" handledBy Bar.factory) ~
     ("debug" handledBy Debug.factory),
      new Base with WhatIsItLike with WhoWantsIt {})

  object Foo extends Responder[Request, Response] {
    type Env = WhatIsItLike
    
    import ResponseHeader._
    
    def render(env: Env)(request: Request) =
      Response(OK, 
        List[(ResponseHeader, String)]((generalToResponse(Pragma) -> "no-cache"))
      , env.yeah.getBytes.toStream)
  }

  object Bar extends Responder[Request, Response] {
    type Env = WhoWantsIt

    def render(env: Env)(request: Request) =
      Response(OK, Nil, env.meh.getBytes.toStream)
  }

  object Debug extends Responder[Request, Response] {
    type Env = Base

    def render(env:Env)(request:Request) = {

      val out = List(
        request.line.method,
        request.line.uri.path,
        request.line.uri.queryString,
        request.line.version.major,
        request.line.version.minor,
        request.headers.mkString("\n")
      ).mkString("\n\n")

      Response(OK, Nil, out.getBytes.toStream)
    }
  }

}
