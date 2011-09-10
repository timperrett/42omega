package ftw

trait Runner[A, B <: Response] {
  def run[E <: BaseEnv](hp: HandledPaths[A, B, E], env: E)
}

object Runner {
  def run[A, B <: Response, E <: BaseEnv](hp: HandledPaths[A, B, E], env: E)(implicit r: Runner[A, B]) =
    r.run(hp, env)
}
