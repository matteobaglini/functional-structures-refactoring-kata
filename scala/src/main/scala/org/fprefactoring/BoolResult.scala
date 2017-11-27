package org.fprefactoring

// BoolResult is an interface/contract and it's called a Data Type.
// It's modelled as an Algebraic Data Type (ADT) of type Sum/Union Type.
trait BoolResult[+A] {
  // all the defined function represents the ADT's combinators
  def map[B](f: A => B): BoolResult[B]
  def flatMap[B](f: A => BoolResult[B]): BoolResult[B]
  def apply[B](cfab: BoolResult[A => B]): BoolResult[B]
  def map2[B, C](cb: BoolResult[B])(f: (A, B) => C): BoolResult[C]
}
// TrueResult and FalseResult are concrete objects and they are called Data Constructors.
case class TrueResult[A](value: A) extends BoolResult[A] {
  override def map[B](f: (A) => B): BoolResult[B] = TrueResult(f(value))
  override def flatMap[B](f: (A) => BoolResult[B]): BoolResult[B] = f(value)
  override def apply[B](cfab: BoolResult[(A) => B]): BoolResult[B] = (this, cfab) match {
    case (_, TrueResult(f)) => TrueResult(f(value))
    case (_, FalseResult()) => FalseResult()
  }
  override def map2[B, C](cb: BoolResult[B])(f: (A, B) => C): BoolResult[C] = (this, cb) match {
    case (_, TrueResult(b)) => TrueResult(f(value, b))
    case (_, FalseResult()) => FalseResult()
  }
}
case class FalseResult[A]() extends BoolResult[A] {
  override def map[B](f: (A) => B): BoolResult[B] = FalseResult()
  override def flatMap[B](f: (A) => BoolResult[B]): BoolResult[B] = FalseResult()
  override def apply[B](cfab: BoolResult[(A) => B]): BoolResult[B] = FalseResult()
  override def map2[B, C](cb: BoolResult[B])(f: (A, B) => C): BoolResult[C] = FalseResult()
}
