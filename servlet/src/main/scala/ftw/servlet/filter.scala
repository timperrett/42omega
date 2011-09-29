package ftw.servlet

import ftw._, http._, request._, response._
import javax.servlet.{Filter, ServletRequest, ServletResponse, FilterChain, FilterConfig}
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}
import scala.collection.JavaConverters._

trait OmegaFilter extends Filter {

  def routingAndEnv: (Handled[Request, Response, E], E) forSome { type E }
  
  def init(config: FilterConfig){}
  
  def destroy(){}
  
  protected def respAndResponse(req: ServletRequest, resp: ServletResponse) = 
    (req, resp) match {
      case (request: HttpServletRequest, response: HttpServletResponse) => 
        Some((request, response))
      case _ => None
    }
    
  
  def doFilter(req: ServletRequest, resp: ServletResponse, chain: FilterChain){
    
    def headers(request: HttpServletRequest): List[(RequestHeader, String)] = for {
      name <- request.getHeaderNames.asInstanceOf[java.util.Enumeration[String]].asScala.toList
      header <- RequestHeader.StringRequestHeader(name).toList
      value <- request.getHeaders(name).asInstanceOf[java.util.Enumeration[String]].asScala
    } yield (header, value)
    
    def line(request: HttpServletRequest): Option[Line] = for {
      method <- Method.StringMethod(request.getMethod)
      uri = Uri.uri(request.getRequestURI, Option(request.getQueryString))
      version <- Version.StringVersion(request.getProtocol)
    } yield Line(method, uri, version)
    
    (for {
      (request, response) <- respAndResponse(req, resp)
      h = headers(request)
      l <- line(request)
      r = new Request(l, h, isToStream(request.getInputStream)) 
      (routing, environment) = routingAndEnv
//      factory <- routing.paths.get(r.line.uri.parts)
      factory <- routing.handled.find(_._1.matches(r)).map(_._2)
    } yield {
      val renderer = factory()
      val result = renderer.render(environment.asInstanceOf[renderer.Env])(r)
      
      // now we've done the awesome, we need to set the params to
      // the output type
      response.setStatus(result.status.toInt)
      result.headers.foreach {
        case (header, value) => response.addHeader(header.asString, value)
      }
      
      val os = resp.getOutputStream
      os.write(result.body.toArray)
    }).getOrElse(chain.doFilter(req, resp))
    
  }
  
  import java.io.InputStream
  
  private def isToStream(is: InputStream): Stream[Byte] = {
    val b = is.read()
    val o = if (b >= 0) b.toByte #:: isToStream(is)
    else Stream.empty
    is.close()
    o
  }
}

trait RawOmegaFilter extends Filter {

  def routingAndEnv: (Handled[(HttpServletRequest, HttpServletResponse), Unit, E], E) forSome { type E }

  def path(p:String) = Vector(p.split("/").filterNot(_.isEmpty) :_*)

  def init(filterConfig: FilterConfig) {}

  final def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
    (request, response) match {
      case (req:HttpServletRequest, res:HttpServletResponse) =>
        doFilter(req, res)
      case _ => chain.doFilter(request, response)
    }
  }

  def doFilter(req:HttpServletRequest, res:HttpServletResponse){
    val (routing, environment) = routingAndEnv

    routing.handled.find(_._1.matches(req -> res)).map{ case (_, factory) =>
      val env = environment.asInstanceOf[responder.Env]
      val responder = factory()
      responder.render(env)(req -> res)
    }.getOrElse{
      res.setStatus(404)
    }

  }

  def destroy() {}
}
