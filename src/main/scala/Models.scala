object Models {

  case class CustomerId(value: String)

  case class CartId(value: String)
  case class Cart(id: CartId, customerId: CustomerId, amount: Double)
  object Cart {
    val missingCart = new Cart(CartId(""), CustomerId(""), 0)
  }

  case class DiscountRule(f: Cart => Double) extends (Cart => Double) {
    def apply(c: Cart): Double = f(c)
  }

}
