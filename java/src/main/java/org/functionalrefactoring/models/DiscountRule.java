package org.functionalrefactoring.models;

import java.util.Objects;
import java.util.function.Function;

public class DiscountRule implements Function<Cart, Amount> {
    public static final DiscountRule NoDiscount = new DiscountRule(c -> {
        throw new IllegalStateException();
    });

    private final Function<Cart, Amount> f;

    public DiscountRule(Function<Cart, Amount> f) {
        this.f = f;
    }

    @Override
    public Amount apply(Cart cart) {
        return f.apply(cart);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountRule that = (DiscountRule) o;
        return Objects.equals(f, that.f);
    }

    @Override
    public int hashCode() {
        return Objects.hash(f);
    }
}
