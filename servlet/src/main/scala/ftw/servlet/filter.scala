package ftw.servlet

import ftw._, http._
import javax.servlet.{Filter, ServletRequest, ServletResponse, FilterChain, FilterConfig}
import javax.servlet.http.{HttpServletRequest}

trait OmegaFilter extends Filter {
  
  def routingAndEnv: (HandledPaths[Unit, HttpResponse, E], E) forSome { type E }
  
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
