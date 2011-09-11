package ftw.examples.servlet

import ftw._, http._, servlet._, request._, response._

trait WhatIsItLike {
  def yeah: String = "Like a BOSS!!!"
}

trait WhoWantsIt {
  def meh = "We do!"
}

class LikeABoss extends OmegaFilter {

  def routingAndEnv =
    (("foo" handledBy Foo.factory) ~ ("bar" handledBy Bar.factory),
      new WhatIsItLike with WhoWantsIt {})

  object Foo extends Responder[Unit, HttpResponse] {
    type Env = WhatIsItLike

    def render(env: Env)(u: Unit) =
      HttpResponse(OK, Nil, env.yeah.getBytes.toStream)
  }

  object Bar extends Responder[Unit, HttpResponse] {
    type Env = WhoWantsIt

    def render(env: Env)(u: Unit) =
      HttpResponse(OK, Nil, env.meh.getBytes.toStream)
  }

}
