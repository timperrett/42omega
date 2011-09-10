package ftw.http.request

/**
 * HTTP request URI.
 * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html#sec5.1.2">RFC 2616 Section 5.1.2 Request-URI</a>.
 *
 * @author <a href="mailto:code@tmorris.net">Tony Morris</a>
 */
sealed trait Uri {
  /**
   * The path of this request URI.
   */
  val path: List[Char]

  /**
   * The query string of this request URI.
   */
  val queryString: Option[List[Char]]

  import Uri.uri

  /**
   * Returns a request URI with the given path and this query string.
   */
  def apply(p: List[Char]) = uri(p, queryString)

  /**
   * Returns a request URI with the given potential query string and this path.
   */
  def apply(q: Option[List[Char]]) = uri(path, q)

  /**
   * Returns the path extension - characters after the last dot (.) in the path.
   */
  lazy val pathExtension = path.dropWhile(_ != '.').reverse.takeWhile(_ != '.').reverse.mkString
  
  lazy val parts : List[String] = {
    path.reverse.foldLeft(List[List[Char]](Nil))((rs, ch) => {
      if (ch == '/') { Nil :: rs } else { (ch :: rs.head)  :: rs.tail }
    }).filter(!_.isEmpty).map(_.mkString)
  }

  /**
   * Returns the query string split into values by <code>'='</code>.
   */
  // lazy val parameters = queryString ∘ (Util.parameters(_))

  /**
   * Returns the query string split into values by <code>'='</code> backed with a hash map.
   */
  // lazy val parametersMap = parameters ∘ (asHashMap[List, NonEmptyList](_))

  /**
   * Returns the query string split into values by <code>'='</code> (removing duplicate values for a given key) backed
   * with a hash map.
   */
  // lazy val parametersMapHeads = parametersMap ∘ (mapHeads(_))
}

/*
 * HTTP request URI.
 * <a href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html#sec5.1.2">RFC 2616 Section 5.1.2 Request-URI</a>.
 */
object Uri {
  /**
   * An extractor that always matches with the URI path and query string.
   */
  def unapply(uri: Uri): Option[(List[Char], Option[List[Char]])] =
    Some(uri.path, uri.queryString)

  /**
   * Constructs a URI with the given path and query string.
   */
  def uri(p: List[Char], s: Option[List[Char]]) = new Uri {
    val path = p
    val queryString = s
  }
}
