package org.fprefactoring

trait Storage[A] {
  def flush(a: A): Unit
}
