object Models {

  case class CustomerId(value: String)

  case class CartId(value: String)
  case class Cart(id: CartId, customerId: CustomerId, amount: Double)

  case class DiscountRule(f: Cart => Double) extends (Cart => Double) {
    def apply(c: Cart): Double = f(c)
  }

}
