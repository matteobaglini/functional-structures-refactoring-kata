package org.fprefactoring

import org.fprefactoring.Models.{Cart, CartId, CustomerId, DiscountRule}

object App {

  def applyDiscount(cartId: CartId, storage: Storage[Cart]): Unit = {
    val cart: Cart = loadCart(cartId)
    if (cart != Cart.missingCart) {
      val rule: DiscountRule = lookupCustomerDiscountRule(cart.customerId)
      if (rule != DiscountRule.noDiscount) {
        val discount: BigDecimal = rule(cart)
        val updatedCart: Cart = updateAmount(cart, discount)
        save(updatedCart, storage)
      }
    }
  }

  def loadCart(id: CartId): Cart =
    if (id.value.contains("gold")) Cart(id, CustomerId("gold-customer"), 100)
    else if (id.value.contains("normal")) Cart(id, CustomerId("normal-customer"), 100)
    else Cart.missingCart

  def lookupCustomerDiscountRule(id: CustomerId): DiscountRule =
    if (id.value.contains("gold")) DiscountRule(half)
    else DiscountRule.noDiscount

  def half(cart: Cart): BigDecimal =
    cart.amount / 2

  def updateAmount(cart: Cart, discount: BigDecimal): Cart =
    cart.copy(id = cart.id, customerId = cart.customerId, amount = cart.amount - discount)

  def save(cart: Cart, storage: Storage[Cart]): Unit =
    storage.flush(cart)
}
