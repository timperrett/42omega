package ftw
package transformer

trait Transformer[-A] {
  def apply(a: A): Stream[Byte]
}

object Transformer {
  implicit object StringTransformer extends Transformer[String] {
    def apply(str: String) = Stream(str.getBytes: _*)
  }
  implicit object NodeSeqTransformer extends Transformer[xml.NodeSeq] {
    def apply(ns: xml.NodeSeq) = Stream(ns.toString.getBytes: _*)
  }
}

trait Transformable {
  def toByteChunk[A](a:A)(implicit t:Transformer[A]):Stream[Byte] = t(a)
}
