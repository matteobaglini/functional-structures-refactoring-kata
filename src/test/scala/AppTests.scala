import App._
import Models._
import org.scalatest.FunSuite

class AppTests extends FunSuite {

  test("happy path") {
    val cartId = CartId("some-gold-cart")
    val storage = new SpyStorage

    applyDiscount(cartId, storage)

    assert(storage.saved.get == Order(OrderId("ZXC482764JN"), CustomerId("gold-customer"), 50))
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

  class SpyStorage extends Storage[Order] {
    var saved: Option[Order] = None
    override def flush(a: Order): Unit = saved = Some(a)
  }
}
