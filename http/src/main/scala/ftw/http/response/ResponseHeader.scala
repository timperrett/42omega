package ftw.http.response

import ftw.http._

/**
 * HTTP response headers.
 * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14">RFC 2616 Section 14 Header Field Definitions</a>.
 *
 * @author <a href="mailto:code@tmorris.net">Tony Morris</a>
 */
sealed trait ResponseHeader {
  /**
   * A string representation of this request header.
   */
  val asString: String

  /**
   * Returns <code>true</code> if this header is an <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec7.html#sec7.1">entity header</a>,
   * <code>false</code> otherwise.
   */
  lazy val isEntity = this match {
    case Entity(_) => true
    case _ => false
  }

  /**
   * Returns <code>true</code> if this header is a <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html#sec4.5">general header</a>,
   * <code>false</code> otherwise.
   */
  lazy val isGeneral = this match {
    case General(_) => true
    case _ => false
  }

  /**
   * Returns the result of the given function to this header if it is an
   * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec7.html#sec7.1">entity header</a>, otherwise returns the given value.
   */
  def entity[X](f: EntityHeader => X, x: => X) = this match {
    case Entity(h) => f(h)
    case _ => x
  }

  /**
   * Returns the result of the given function to this header if it is a
   * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec4.html#sec4.5">general header</a>, otherwise returns the given value.
   */
  def general[X](f: GeneralHeader => X, x: => X) = this match {
    case General(h) => f(h)
    case _ => x
  }
}
/**
 * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.5">§</a>
 */
final case object AcceptRanges extends ResponseHeader {
  override val asString = "Accept-Ranges"
}
/**
 * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.6">§</a>
 */
final case object Age extends ResponseHeader {
  override val asString = toString
}
/**
 * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.19">§</a>
 */
final case object ETag extends ResponseHeader {
  override val asString = toString
}
/**
 * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.30">§</a>
 */
final case object Location extends ResponseHeader {
  override val asString = toString
}
/**
 * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.33">§</a>
 */
final case object ProxyAuthenticate extends ResponseHeader {
  override val asString = "Proxy-Authenticate"
}
/**
 * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.37">§</a>
 */
final case object RetryAfter extends ResponseHeader {
  override val asString = "Retry-After"
}
/**
 * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.38">§</a>
 */
final case object Server extends ResponseHeader {
  override val asString = toString
}
/**
 * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.44">§</a>
 */
final case object Vary extends ResponseHeader {
  override val asString = toString
}
/**
 * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.47">§</a>
 */
final case object WWWAuthenticate extends ResponseHeader {
  override val asString = "WWW-Authenticate"
}
private final case class Entity(eh: EntityHeader) extends ResponseHeader {
  override val asString = eh.asString
}
private final case class General(gh: GeneralHeader) extends ResponseHeader {
  override val asString = gh.asString
}
