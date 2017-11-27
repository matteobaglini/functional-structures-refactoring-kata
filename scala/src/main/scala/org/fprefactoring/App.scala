package org.fprefactoring

import org.fprefactoring.Models._

object App {

  trait BoolResult[+A] {
    def modify[B](f: A => B): BoolResult[B]
    def modifyAndReduce[B](f: A => BoolResult[B]): BoolResult[B]
  }
  case class TrueResult[A](value: A) extends BoolResult[A] {
    override def modify[B](f: (A) => B): BoolResult[B] = TrueResult(f(value))
    override def modifyAndReduce[B](f: (A) => BoolResult[B]): BoolResult[B] = f(value)
  }
  case class FalseResult[A]() extends BoolResult[A] {
    override def modify[B](f: (A) => B): BoolResult[B] = FalseResult()
    override def modifyAndReduce[B](f: (A) => BoolResult[B]): BoolResult[B] = FalseResult()
  }

  def applyDiscount(cartId: CartId, storage: Storage[Cart]): Unit = {
    val cart = loadCartResult(cartId)
    val rule = cart.modifyAndReduce(c => lookupDiscountRuleResult(c))
    val discount = cart.modifyAndReduce(c => rule.modify(r => r(c)))
    val updatedCart = cart.modifyAndReduce(c => discount.modify(d => updateAmount(c, d)))
    updatedCart.modify(uc => save(uc, storage))
  }

  def applyDiscountIdeal(cartId: CartId, storage: Storage[Cart]): Unit = {
    val cart = loadCart(cartId)
    val rule = lookupDiscountRule(cart.customerId)
    val discount = rule(cart)
    val updatedCart = updateAmount(cart, discount)
    save(updatedCart, storage)
  }

  def applyDiscountIfs(cartId: CartId, storage: Storage[Cart]): Unit = {
    val cart = loadCart(cartId)
    if (cart != Cart.missingCart) {
      val rule = lookupDiscountRule(cart.customerId)
      if (rule != DiscountRule.noDiscount) {
        val discount = rule(cart)
        val updatedCart = updateAmount(cart, discount)
        save(updatedCart, storage)
      }
    }
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
