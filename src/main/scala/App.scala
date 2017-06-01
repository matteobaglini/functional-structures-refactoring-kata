import Models._

object App {

  def applyDiscount(cartId: CartId, storage: Storage[Order]): Unit = {
    val cart: Cart = loadCart(cartId)
    if (cart != Cart.missingCart) {
      val rule: DiscountRule = lookupCustomerDiscountRule(cart.customerId)
      if (rule != DiscountRule.noDiscount) {
        val discount: Double = rule(cart)
        val order: Order = makeOrder(cart, discount)
        saveOrder(order, storage)
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

  def half(cart: Cart): Double =
    cart.amount / 2

  def makeOrder(cart: Cart, discount: Double): Order =
    Order(OrderId("ZXC482764JN"), cart.customerId, cart.amount - discount)

  def saveOrder(order: Order, storage: Storage[Order]): Unit =
    storage.flush(order)
}
