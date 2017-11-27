package org.fprefactoring

import org.fprefactoring.Models._

object App {

  trait BoolResult[+A] {
    def map[B](f: A => B): BoolResult[B]
    def flatMap[B](f: A => BoolResult[B]): BoolResult[B]
  }
  case class TrueResult[A](value: A) extends BoolResult[A] {
    override def map[B](f: (A) => B): BoolResult[B] = TrueResult(f(value))
    override def flatMap[B](f: (A) => BoolResult[B]): BoolResult[B] = f(value)
  }
  case class FalseResult[A]() extends BoolResult[A] {
    override def map[B](f: (A) => B): BoolResult[B] = FalseResult()
    override def flatMap[B](f: (A) => BoolResult[B]): BoolResult[B] = FalseResult()
  }

  def applyDiscount(cartId: CartId, storage: Storage[Cart]): Unit = {
    for {
      cart <- loadCartResult(cartId)
      rule <- lookupDiscountRuleResult(cart)
      discount = rule(cart)
      updated = updateAmount(cart, discount)
      _ = save(updated, storage)
    } yield ()
  }

  def applyDiscountManual(cartId: CartId, storage: Storage[Cart]): Unit = {
    val cart = loadCartResult(cartId)
    val rule = cart.flatMap(c => lookupDiscountRuleResult(c))
    val discount = cart.flatMap(c => rule.map(r => r(c)))
    val updatedCart = cart.flatMap(c => discount.map(d => updateAmount(c, d)))
    updatedCart.map(uc => save(uc, storage))
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

  import scala.util.{Failure, Success, Try}

  private def loadCartResult(cartId: CartId): Try[Cart] = {
    val cart = loadCart(cartId)
    if (cart != Cart.missingCart) Success(cart) else Failure(new RuntimeException("missing cart"))
  }

  private def lookupDiscountRuleResult(cart: Cart): Try[DiscountRule] = {
    val rule = lookupDiscountRule(cart.customerId)
    if (rule != DiscountRule.noDiscount) Success(rule) else Failure(new RuntimeException("no discount"))
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
