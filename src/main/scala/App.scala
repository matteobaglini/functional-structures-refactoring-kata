import Models._

object App {

  def applyDiscount(cartId: CartId, storage: Storage[Cart]): Unit = {
    val cart: Cart = loadCart(cartId)
    if (cart != Cart.missingCart) {
      val rule: Option[DiscountRule] = lookupCustomerDiscountRule(cart.customerId)
      if (rule.isDefined) {
        val discount: Double = rule.get(cart)
        val updatedCart: Cart = updateAmount(cart, discount)
        save(updatedCart, storage)
      }
    }
  }

  def loadCart(id: CartId): Cart =
    if (id.value.contains("gold")) Cart(id, CustomerId("gold-customer"), 100)
    else if (id.value.contains("normal")) Cart(id, CustomerId("normal-customer"), 100)
    else Cart.missingCart

  def lookupCustomerDiscountRule(id: CustomerId): Option[DiscountRule] =
    if (id.value.contains("gold")) Some(DiscountRule(half))
    else None

  def half(cart: Cart): Double =
    cart.amount / 2

  def updateAmount(cart: Cart, discount: Double): Cart =
    cart.copy(id = cart.id, customerId = cart.customerId, amount = cart.amount - discount)

  def save(cart: Cart, storage: Storage[Cart]): Unit =
    storage.flush(cart)
}
