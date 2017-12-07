package org.functionalrefactoring.models

data class DiscountRule(private val f: (Cart) -> (Amount)) {

    fun apply(cart: Cart): Amount {
        return f(cart)
    }

    companion object {
        val NoDiscount = DiscountRule({ _ -> throw IllegalStateException() })
    }
}

