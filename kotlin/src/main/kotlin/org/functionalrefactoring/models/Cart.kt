package org.functionalrefactoring.models

data class Cart(val id: CartId, val customerId: CustomerId, val amount: Amount) {
    companion object {
        val MissingCart = Cart(CartId(), CustomerId(), Amount())
    }
}
