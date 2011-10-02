package ftw

trait Responder[-A, +B, -V] { outer =>
  type Env

  def respond(env: Env, req: A, value:V): B
  
  def factory: ResponderFactory[A, B, V, this.type] = new ResponderFactory[A, B, V, this.type] {
    def responder = outer
  }
}

trait ResponderFactory[-A, +B, -V, +R <: Responder[A, B, V]] {
  def responder: R
}

trait Route[-A, +V] { R =>
  def unapply(a:A):Option[V]
  
  def handledBy[A2 <: A, B, R <: Responder[A2, B, V]](resp: ResponderFactory[A2, B, V, R]): Handled[A2, B, R#Env] =
    Handled[A2, B, R#Env]( e => {
      case a @ R(v) => resp.asInstanceOf[ResponderFactory[A2, B, V, Responder[A2, B, V]{ type Env = R#Env}]].responder.respond(e, a, v)
    })
}

case class Handled[-A, +B, E](handled: E => PartialFunction[A, B]) extends (E => PartialFunction[A, B]){
  def ~[A2 <: A, B2 >: B, E2](other: Handled[A2, B2, E2]): Handled[A2, B2, E with E2] =
    Handled[A2, B2, E with E2]{ e => handled(e) orElse other(e) }

  def apply(env:E) = handled(env)
}