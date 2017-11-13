package org.functionalrefactoring.models;

import java.math.BigDecimal;
import java.util.Objects;

public class Cart {
    public static final Cart MissingCart = new Cart(new CartId(""), new CustomerId(""), new Amount(new BigDecimal(0)));

    public final CartId id;
    public final CustomerId customerId;
    public final Amount amount;

    public Cart(CartId id, CustomerId customerId, Amount amount) {
        this.id = id;
        this.customerId = customerId;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(id, cart.id) &&
                Objects.equals(customerId, cart.customerId) &&
                Objects.equals(amount, cart.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, amount);
    }
}
