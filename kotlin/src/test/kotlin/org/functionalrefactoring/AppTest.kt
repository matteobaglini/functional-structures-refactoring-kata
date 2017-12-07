package org.functionalrefactoring

import org.functionalrefactoring.models.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.assertThat
import org.junit.Test
import java.math.BigDecimal

class AppTest {

    @Test
    fun happyPath() {
        val cartId = CartId("some-gold-cart")
        val storage = SpyStorage()

        App.applyDiscount(cartId, storage)

        val expected = Cart(CartId("some-gold-cart"), CustomerId("gold-customer"), Amount(BigDecimal(50)))
        assertThat(storage.saved, `is`(expected))
    }

    @Test
    fun noDiscount() {
        val cartId = CartId("some-normal-cart")
        val storage = SpyStorage()

        App.applyDiscount(cartId, storage)

        assertThat(storage.saved, nullValue())
    }

    @Test
    fun missingCart() {
        val cartId = CartId("missing-cart")
        val storage = SpyStorage()

        App.applyDiscount(cartId, storage)

        assertThat(storage.saved, nullValue())
    }

    internal inner class SpyStorage : Storage<Cart> {
        var saved: Cart? = null

        override fun flush(item: Cart) {
            saved = item
        }
    }
}

