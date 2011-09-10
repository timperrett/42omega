package ftw

trait Responder[-A, +B] {
  type Env
  
  def render(env: Env)(req: A): B
}

trait ResponderFactory[-A, +B, +R <: Responder[A, B]] {
  def apply(): R
}

case class Path(parts: Vector[String]) {
  def /(path: String): Path =
    Path(parts :+ path)
  
  def handledBy[A, B, R <: Responder[A, B]](resp: ResponderFactory[A, B, R]): HandledPaths[A, B, R#Env] =
    new HandledPaths[A, B, R#Env](Map(parts -> resp.asInstanceOf[ResponderFactory[A, B, Responder[A, B] { type Env = R#Env }]]))
}

case class HandledPaths[-A, +B, E](paths: Map[Vector[String], ResponderFactory[A, B, Responder[A, B] { type Env = E }]]) {
  private type Disappointment[A2, B2, E2] = Map[Vector[String], ResponderFactory[A2, B2, Responder[A2, B2] { type Env = E with E2 }]]
   
  def ~[A2 <: A, B2 >: B, E2](hp: HandledPaths[A2, B2, E2]): HandledPaths[A2, B2, E with E2] =
    HandledPaths[A2, B2, E with E2]((paths ++ hp.paths).asInstanceOf[Disappointment[A2, B2, E2]])
}

trait Runner[A, B] {
  def run[E](hp: HandledPaths[A, B, E], env: E)
}

object Runner {
  def run[A, B, E](hp: HandledPaths[A, B, E], env: E)(implicit r: Runner[A, B]) =
    r.run(hp, env)
}
