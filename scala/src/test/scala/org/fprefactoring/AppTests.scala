package org.fprefactoring

import org.fprefactoring.App._
import org.fprefactoring.Models._
import org.scalatest.FunSuite

class AppTests extends FunSuite {

  test("happy path") {
    val cartId = CartId("some-gold-cart")
    val storage = new SpyStorage

    applyDiscount(cartId, storage)

    val expected = Cart(CartId("some-gold-cart"), CustomerId("gold-customer"), Amount(50))
    assert(storage.saved.get == expected)
  }

  test("no discount") {
    val cartId = CartId("some-normal-cart")
    val storage = new SpyStorage

    applyDiscount(cartId, storage)

    assert(storage.saved.isEmpty)
  }

  test("missing cart") {
    val cartId = CartId("missing-cart")
    val storage = new SpyStorage

    applyDiscount(cartId, storage)

    assert(storage.saved.isEmpty)
  }

  class SpyStorage extends Storage[Cart] {
    var saved: Option[Cart] = None
    override def flush(value: Cart): Unit = saved = Some(value)
  }
}
