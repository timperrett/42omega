package org

package object ftw {
  implicit def strToPath(str: String): Path = Path(Vector(str))
}
