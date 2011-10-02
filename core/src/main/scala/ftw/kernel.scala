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

trait Matcher[-A] { matcher =>
  def matches(a:A):Boolean 
  
  def handledBy[A2 <: A, B, R <: Responder[A2, B]](resp: ResponderFactory[A2, B, R]): Handled[A2, B, R#Env] =
    new Handled[A2, B, R#Env](Seq(matcher -> resp.asInstanceOf[ResponderFactory[A2, B, Responder[A2, B] { type Env = R#Env }]]))
}

case class Handled[-A, +B, E](handled: Seq[(Matcher[A], ResponderFactory[A, B, Responder[A, B] { type Env = E }])]) extends (E => PartialFunction[A, B]){
  private type Disappointment[A2, B2, E2] = Seq[(Matcher[A2], ResponderFactory[A2, B2, Responder[A2, B2] { type Env = E with E2 }])]
   
  def ~[A2 <: A, B2 >: B, E2](other: Handled[A2, B2, E2]): Handled[A2, B2, E with E2] =
    Handled[A2, B2, E with E2]((handled ++ other.handled).asInstanceOf[Disappointment[A2, B2, E2]])

  private object Responder {
    def unapply(a:A) = handled.find(_._1.matches(a)).map(_._2.responder)
  }

  def apply(env:E) = {
    case a@Responder(responder) => responder.respond(env)(a)
  }
}