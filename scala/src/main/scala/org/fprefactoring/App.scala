package org.fprefactoring

import org.fprefactoring.Models._

object App {

  def applyDiscount(cartId: CartId, storage: Storage[Cart]): Unit = {
    val cart = loadCartResult(cartId)
    val rule = cart.flatMap(c => lookupDiscountRuleResult(c))
    val discount = cart.apply(rule)
    val updatedCart = cart.map2(discount)(updateAmount)
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
