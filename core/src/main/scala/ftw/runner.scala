package ftw

trait Runner[A, B] {
  def run[E <: BaseEnv](hp: HandledPaths[A, B, E], env: E)
}

object Runner {
  def run[A, B, E <: BaseEnv](hp: HandledPaths[A, B, E], env: E)(implicit r: Runner[A, B]) =
    r.run(hp, env)
}
