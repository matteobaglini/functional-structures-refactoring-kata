package org.fprefactoring

object Models {
  case class Amount(value: BigDecimal)

  case class CustomerId(value: String)
  case class CartId(value: String)
  case class Cart(id: CartId, customerId: CustomerId, amount: Amount)
  object Cart {
    val missingCart = new Cart(CartId(""), CustomerId(""), Amount(0))
  }

  case class DiscountRule(f: Cart => Amount) extends (Cart => Amount) {
    def apply(c: Cart): Amount = f(c)
  }
  object DiscountRule {
    val noDiscount = DiscountRule(_ => throw new RuntimeException("no discount"))
  }
}
