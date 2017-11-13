package org.functionalrefactoring.models;

import java.util.Objects;

public class CustomerId {
    public final String value;

    public CustomerId(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerId cartId = (CustomerId) o;
        return Objects.equals(value, cartId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
