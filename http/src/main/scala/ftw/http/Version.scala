package ftw.http

import java.lang.Character.isDigit

/**
 * HTTP version.
 * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.1">RFC 2616 Section 3.1 HTTP Version</a>.
 *
 * @author <a href="mailto:code@tmorris.net">Tony Morris</a>
 */
sealed trait Version {
  /**
   * HTTP Version major number.
   */
  val major: Digit

  /**
   * HTTP Version major number.
   */
  val minor: Digit

  /**
   * A string representation of this version
   */
  def asString = "HTTP/" + major.toInt + "." + minor.toInt
}

/**
 * HTTP version.
 * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.1">RFC 2616 Section 3.1 HTTP Version</a>.
 */
object Version {
  /**
   * Extracts the major and minor numbers of the given version.
   */
  def unapply(v: Version): Option[(Digit, Digit)] = Some((v.major, v.minor))

  /**
   * Constructs a version with the given major and minor numbers.
   */
  def version(maj: Digit, min: Digit): Version = new Version {
    val major = maj
    val minor = min
  }
  
  /**
   * A version for HTTP/1.0.
   */
  import Digit._

  val version10 = version(_1, _0)

  /**
   * A version for HTTP/1.1.
   */
  val version11 = version(_1, _1)
}
