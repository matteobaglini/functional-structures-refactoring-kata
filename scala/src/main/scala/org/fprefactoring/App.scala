package org.fprefactoring

import org.fprefactoring.Models._

object App {

  trait LoadResult[+A]
  case class FoundResult[A](value: A) extends LoadResult[A]
  case class NotFoundResult[A]() extends LoadResult[A]

  def applyDiscount(cartId: CartId, storage: Storage[Cart]): Unit = {
    val cart = loadCart(cartId)
    val loadResult = if (cart != Cart.missingCart) FoundResult(cart) else NotFoundResult()
    if (cart != Cart.missingCart) {
      val rule = lookupDiscountRule(cart.customerId)
      val lookupResult = if (rule != DiscountRule.noDiscount) FoundResult(rule) else NotFoundResult()
      if (rule != DiscountRule.noDiscount) {
        val discount = rule(cart)
        val updatedCart = updateAmount(cart, discount)
        save(updatedCart, storage)
      }
    }
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
