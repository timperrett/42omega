package ftw

trait Responder[-A, +B, -V] {
  type Env

  def respond(env: Env, req: A, value:V): B
}

trait Route[-A, +V] { R =>
  def unapply(a:A):Option[V]
  
  def handledBy[A2 <: A, B, E](resp: Responder[A2, B, V]{ type Env = E }): Handled[A2, B, E] =
    Handled[A2, B, E]( e => { case a @ R(v) => resp.respond(e, a, v) })
}

case class Handled[-A, +B, E](handled: E => PartialFunction[A, B]) extends (E => PartialFunction[A, B]){
  def ~[A2 <: A, B2 >: B, E2](other: Handled[A2, B2, E2]): Handled[A2, B2, E with E2] =
    Handled[A2, B2, E with E2]( e => handled(e) orElse other(e) )

  def apply(env:E) = handled(env)
}