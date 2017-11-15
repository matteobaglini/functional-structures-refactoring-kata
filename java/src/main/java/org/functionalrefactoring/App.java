package org.functionalrefactoring;

import org.functionalrefactoring.models.*;

import java.math.BigDecimal;

public class App {
    public static void applyDiscount(CartId cartId, Storage<Cart> storage) {
        Cart cart = loadCart(cartId);
        if (cart != Cart.MissingCart) {
            DiscountRule rule = lookupDiscountRule(cart.customerId);
            if (rule != DiscountRule.NoDiscount) {
                Amount discount = rule.apply(cart);
                Cart updatedCart = updateAmount(cart, discount);
                save(updatedCart, storage);
            }
        }
    }

    private static Cart loadCart(CartId id) {
        if (id.value.contains("gold"))
            return new Cart(id, new CustomerId("gold-customer"), new Amount(new BigDecimal(100)));
        if (id.value.contains("normal"))
            return new Cart(id, new CustomerId("normal-customer"), new Amount(new BigDecimal(100)));
        return Cart.MissingCart;
    }

    private static DiscountRule lookupDiscountRule(CustomerId id) {
        if (id.value.contains("gold")) return new DiscountRule(App::half);
        return DiscountRule.NoDiscount;
    }

    private static Cart updateAmount(Cart cart, Amount discount) {
        return new Cart(cart.id, cart.customerId, new Amount(cart.amount.value.subtract(discount.value)));
    }

    private static void save(Cart cart, Storage<Cart> storage) {
        storage.flush(cart);
    }

    private static Amount half(Cart cart) {
        return new Amount(cart.amount.value.divide(new BigDecimal(2)));
    }
}
