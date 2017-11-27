package org.fprefactoring

import org.fprefactoring.Models._

object App {

  trait BoolResult[+A] {
    def map[B](f: A => B): BoolResult[B]
    def flatMap[B](f: A => BoolResult[B]): BoolResult[B]
    def apply[B](cfab: BoolResult[A => B]): BoolResult[B]
    def map2[B, C](cb: BoolResult[B])(f: (A, B) => C): BoolResult[C]
  }
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

  def applyDiscount(cartId: CartId, storage: Storage[Cart]): Unit = {
    val cart = loadCartResult(cartId)
    val rule = cart.flatMap(c => lookupDiscountRuleResult(c))
    val discount = cart.flatMap(c => rule.map(r => r(c)))
    val updatedCart = cart.flatMap(c => discount.map(d => updateAmount(c, d)))
    updatedCart.map(uc => save(uc, storage))
  }

  private def loadCartResult(cartId: CartId): BoolResult[Cart] = {
    val cart = loadCart(cartId)
    if (cart != Cart.missingCart) TrueResult(cart) else FalseResult()
  }

  private def lookupDiscountRuleResult(cart: Cart): BoolResult[DiscountRule] = {
    val rule = lookupDiscountRule(cart.customerId)
    if (rule != DiscountRule.noDiscount) TrueResult(rule) else FalseResult()
  }

  def loadCart(id: CartId): Cart =
    if (id.value.contains("gold")) Cart(id, CustomerId("gold-customer"), Amount(100))
    else if (id.value.contains("normal")) Cart(id, CustomerId("normal-customer"), Amount(100))
    else Cart.missingCart

  def lookupDiscountRule(id: CustomerId): DiscountRule =
    if (id.value.contains("gold")) DiscountRule(half)
    else DiscountRule.noDiscount

  def half(cart: Cart): Amount =
    Amount(cart.amount.value / 2)

  def updateAmount(cart: Cart, discount: Amount): Cart =
    cart.copy(id = cart.id, customerId = cart.customerId, amount = Amount(cart.amount.value - discount.value))

  def save(cart: Cart, storage: Storage[Cart]): Unit =
    storage.flush(cart)
}
