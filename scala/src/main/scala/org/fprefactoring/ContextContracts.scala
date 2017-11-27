package org.fprefactoring

object ContextContracts {

  // Example of boxes (aka Effects):
  //  Option, Future, List, Try, Either

  trait Functor[Box[_]] {
    def map[A, B](ba: Box[A])(f: A => B): Box[B]
  }

  trait Applicative[Box[_]] extends Functor[Box] {
    def pure[A](a: A): Box[A]
    def apply[A, B](bf: Box[A => B])(ba: Box[A]): Box[B]
    def map2[A, B, C](ba: Box[A])(bb: Box[B])(f: (A, B) => C): Box[C]
  }

  trait Monad[Box[_]] extends Applicative[Box] {
    def flatMap[A, B](ca: Box[A])(f: A => Box[B]): Box[B]
  }
}
