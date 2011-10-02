package ftw

trait Responder[-A, +B] { outer =>
  type Env

  def respond(env: Env)(req: A): B
  
  def factory: ResponderFactory[A, B, this.type] = new ResponderFactory[A, B, this.type] {
    def responder = outer
  }
}

trait ResponderFactory[-A, +B, +R <: Responder[A, B]] {
  def responder: R
}

trait Matcher[-A] {
  def matches(a:A):Boolean 
  
  def handledBy[A2 <: A, B, R <: Responder[A2, B]](resp: ResponderFactory[A2, B, R]): Handled[A2, B, R#Env] =
    Handled[A2, B, R#Env]( e => {
      case a if matches(a) => resp.asInstanceOf[ResponderFactory[A2, B, Responder[A2, B]{ type Env = R#Env}]].responder.respond(e)(a)
    })
}

case class Handled[-A, +B, E](handled: E => PartialFunction[A, B]) extends (E => PartialFunction[A, B]){
  def ~[A2 <: A, B2 >: B, E2](other: Handled[A2, B2, E2]): Handled[A2, B2, E with E2] =
    Handled[A2, B2, E with E2]{ e => handled(e) orElse other(e) }

  def apply(env:E) = handled(env)
}