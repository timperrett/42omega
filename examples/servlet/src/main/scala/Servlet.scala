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

  def routing =
    (("foo" handledBy Foo) ~
     ("bar" handledBy Bar) ~
     ("debug" handledBy Debug))(new Base with WhatIsItLike with WhoWantsIt {})

  trait IgnoreParamResponder[-A, +B] extends Responder[A, B, Any] {
    def respond(env: Env, request:A, ignore:Any): B = respond(env, request)
    def respond(env: Env, request:A): B
  }

  object Foo extends IgnoreParamResponder[Request, Response] {
    type Env = WhatIsItLike
    
    import ResponseHeader._
    
    def respond(env: Env, request: Request) =
      Response(OK, 
        List[(ResponseHeader, String)]((generalToResponse(Pragma) -> "no-cache"))
      , env.yeah.getBytes.toStream)
  }

  object Bar extends IgnoreParamResponder[Request, Response] {
    type Env = WhoWantsIt

    def respond(env: Env, request: Request) =
      Response(OK, Nil, env.meh.getBytes.toStream)
  }

  object Debug extends IgnoreParamResponder[Request, Response] {
    type Env = Base

    def respond(env:Env, request:Request) = {

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
