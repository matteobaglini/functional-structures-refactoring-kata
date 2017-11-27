package org.fprefactoring

import org.fprefactoring.Models._

object App {

  trait BoolResult[+A] {
    def modify[B](f: A => B): BoolResult[B]
  }
  case class TrueResult[A](value: A) extends BoolResult[A] {
    override def modify[B](f: (A) => B): BoolResult[B] = TrueResult(f(value))
  }
  case class FalseResult[A]() extends BoolResult[A] {
    override def modify[B](f: (A) => B): BoolResult[B] = FalseResult()
  }

  def applyDiscount(cartId: CartId, storage: Storage[Cart]): Unit = {
    val cart = loadCart(cartId)
    val loadResult = if (cart != Cart.missingCart) TrueResult(cart) else FalseResult()
    if (cart != Cart.missingCart) {
      val rule = lookupDiscountRuleResult(cart)
      val discount = rule.modify(r => r(cart))
      val updatedCart = discount.modify(d => updateAmount(cart, d))
      updatedCart.modify(uc => save(uc, storage))
    }
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
