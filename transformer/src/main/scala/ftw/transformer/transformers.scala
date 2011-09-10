package ftw
package transformer

trait Transformer[-A] {
  def apply(a: A): Stream[Byte]
}


trait Transformable {
  def toByteChunk[A : Transformer](a: A): Stream[Byte]
}

trait StringTransformable extends Transformable {
  implicit object StringTransformer extends Transformer[String] {
    def apply(str: String) = Stream(str.getBytes: _*)
  }
}

trait NodeSeqTransformable extends Transformable {
  implicit object NodeSeqTransformer extends Transformer[xml.NodeSeq] {
    def apply(ns: xml.NodeSeq) = Stream(ns.toString.getBytes: _*)
  }
}
