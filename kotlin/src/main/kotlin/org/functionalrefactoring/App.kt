package org.functionalrefactoring

import org.functionalrefactoring.models.*
import java.math.BigDecimal

object App {
    fun applyDiscount(cartId: CartId, storage: Storage<Cart>) {
        val cart = loadCart(cartId)
        if (cart !== Cart.MissingCart) {
            val rule = lookupDiscountRule(cart.customerId)
            if (rule !== DiscountRule.NoDiscount) {
                val discount = rule.apply(cart)
                val updatedCart = updateAmount(cart, discount)
                save(updatedCart, storage)
            }
        }
    }

    private fun loadCart(id: CartId): Cart {
        if (id.value.contains("gold"))
            return Cart(id, CustomerId("gold-customer"), Amount(BigDecimal(100)))
        return if (id.value.contains("normal")) Cart(id, CustomerId("normal-customer"), Amount(BigDecimal(100))) else Cart.MissingCart
    }

    private fun lookupDiscountRule(id: CustomerId): DiscountRule {
        return if (id.value.contains("gold")) DiscountRule({ cart -> half(cart) }) else DiscountRule.NoDiscount
    }

    private fun updateAmount(cart: Cart, discount: Amount): Cart {
        return Cart(cart.id, cart.customerId, Amount(cart.amount.value.subtract(discount.value)))
    }

    private fun save(cart: Cart, storage: Storage<Cart>) {
        storage.flush(cart)
    }

    private fun half(cart: Cart): Amount {
        return Amount(cart.amount.value.divide(BigDecimal(2)))
    }
}

