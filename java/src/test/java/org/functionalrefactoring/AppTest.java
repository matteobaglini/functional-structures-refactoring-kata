package org.functionalrefactoring;

import org.functionalrefactoring.models.*;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class AppTest {

    @Test
    public void HappyPath()
    {
        CartId cartId = new CartId("some-gold-cart");
        SpyStorage storage = new SpyStorage();

        App.applyDiscount(cartId, storage);

        Cart expected = new Cart(new CartId("some-gold-cart"), new CustomerId("gold-customer"), new Amount(new BigDecimal(50)));
        assertThat(storage.saved, is(expected));
    }

    @Test
    public void NoDiscount()
    {
        CartId cartId = new CartId("some-normal-cart");
        SpyStorage storage = new SpyStorage();

        App.applyDiscount(cartId, storage);

        assertThat(storage.saved, nullValue());
    }

    @Test
    public void MissingCart()
    {
        CartId cartId = new CartId("missing-cart");
        SpyStorage storage = new SpyStorage();

        App.applyDiscount(cartId, storage);

        assertThat(storage.saved, nullValue());
    }

    class SpyStorage implements Storage<Cart> {
        public Cart saved;

        @Override
        public void flush(Cart item) {
            saved = item;
        }
    }
}
