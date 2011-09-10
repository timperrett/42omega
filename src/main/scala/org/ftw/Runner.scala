package org.ftw

import javax.servlet.{Filter, ServletRequest, ServletResponse, FilterChain, FilterConfig}
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

trait Runner[+A, -B] {
  def run[E <: BaseEnv](hp: HandledPaths[A, B, E], env: E)
}

object Runner {
  def run[A, B, E <: BaseEnv](hp: HandledPaths[A, B, E], env: E)(implicit r: Runner[A, B]) =
    r.run(hp, env)
  
  
}


trait OmegaFilter extends Filter {
  
  def routingAndEnv: (HandledPaths[Unit, HttpResponse, E], E) forSome { type E <: BaseEnv }
  
  def init(config: FilterConfig){}
  
  def destroy(){}
  
  def doFilter(req: ServletRequest, resp: ServletResponse, chain: FilterChain){
    val httpReq = req.asInstanceOf[HttpServletRequest]
    
    val (routing, environment) = routingAndEnv
    
    val path = Vector(httpReq.getRequestURI split "/" filterNot { _.isEmpty }: _*)
    val renderer = routing.paths(path)()
    val HttpResponse(contentType, str) = renderer.render(environment.asInstanceOf[renderer.Env])(())
    
    val os = resp.getOutputStream
    os.write(str.toArray)
    
    resp.setContentType(contentType)
  }
}

class LikeABoss extends OmegaFilter {
  def routingAndEnv = 
    ("foo" handledBy FooFactory, new BaseEnv {})
  
  object FooFactory extends ResponderFactory[Unit, HttpResponse, Foo.type] {
    def apply() = Foo
  }
  
  object Foo extends Responder[Unit, HttpResponse] {
    type Env = BaseEnv
    
    def render(env: Env)(u: Unit) =
      HttpResponse("text/plain", "Like a boss!".getBytes.toStream)
  }
}
