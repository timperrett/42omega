package ftw.servlet

import ftw._, http._, request._, response._
import javax.servlet.{Filter, ServletRequest, ServletResponse, FilterChain, FilterConfig}
import javax.servlet.http.{HttpServletRequest}
import scala.collection.JavaConverters._

trait OmegaFilter extends Filter {
  // private def toRequest(request: HttpServletRequest): Request = {
  //   val keys: List[String] = 
  //     request.getHeaderNames.asInstanceOf[java.util.Enumeration[String]].asScala.toList
  //   
  //   val values: List[(RequestHeader)] = keys.flatMap { h =>
  //     request.getHeaders(h).asInstanceOf[java.util.Enumeration[String]].asScala.map(
  //       _.asInstanceOf[String]).filter(_.length > 0).map(v => )
  //   }
  //   
  //   // Request(line, headers)
  // }
    
  
  def routingAndEnv: (HandledPaths[Unit, HttpResponse, E], E) forSome { type E }
  
  def init(config: FilterConfig){}
  
  def destroy(){}
  
  def doFilter(req: ServletRequest, resp: ServletResponse, chain: FilterChain){
    val httpReq = req.asInstanceOf[HttpServletRequest]
    
    println(">>>>>")
    println(httpReq.getHeaderNames.asInstanceOf[java.util.Enumeration[String]].asScala.toList)
    
    
    val (routing, environment) = routingAndEnv
    
    val path = Vector(httpReq.getRequestURI split "/" filterNot { _.isEmpty }: _*)
    val renderer = routing.paths(path)()
    // val HttpResponse(contentType, str) = renderer.render(environment.asInstanceOf[renderer.Env])(())
    
    // val os = resp.getOutputStream
    // os.write(str.toArray)
    
    // resp.setContentType(contentType)
  }
}
