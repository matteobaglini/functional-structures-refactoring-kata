import App._
import Models._
import org.scalatest.FunSuite

class AppTests extends FunSuite {
  val storage = SpyStorage

  test("happy path") {
    val expected = Order(OrderId("ZXC482764JN"), CustomerId("gold-customer"), 50)
    val cartId = CartId("some-gold-cart")

    confirmCart(cartId, storage)

    assert(storage.saved == expected)
  }

  test("no discount") {
    val expected = Order(OrderId("ZXC482764JN"), CustomerId("normal-customer"), 100)
    val cartId = CartId("some-normal-cart")

    confirmCart(cartId, storage)

    assert(storage.saved == expected)
  }

  test("missing cart") {
    val expected = Order(OrderId("ZXC482764JN"), CustomerId("normal-customer"), 100)
    val cartId = CartId("missing-cart")

    confirmCart(cartId, storage)

    assert(storage.saved == expected)
  }

  object SpyStorage extends Storage[Order] {
    var saved = Order(OrderId("wrong"), CustomerId("wrong"), -1)
    override def flush(a: Order): Unit = saved = a
  }
}
