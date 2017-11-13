package org.functionalrefactoring.models;

import java.util.Objects;

public class CartId {
    public final String value;

    public CartId(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartId cartId = (CartId) o;
        return Objects.equals(value, cartId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
