package org.fprefactoring

object Models {

  case class CustomerId(value: String)

  case class CartId(value: String)
  case class Cart(id: CartId, customerId: CustomerId, amount: BigDecimal)
  object Cart {
    val missingCart = new Cart(CartId(""), CustomerId(""), 0)
  }

  case class DiscountRule(f: Cart => BigDecimal) extends (Cart => BigDecimal) {
    def apply(c: Cart): BigDecimal = f(c)
  }
  object DiscountRule {
    val noDiscount = DiscountRule(_ => throw new RuntimeException("no discount"))
  }

}
