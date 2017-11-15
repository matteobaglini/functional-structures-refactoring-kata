using FunctionalRefactoring.Models;

namespace FunctionalRefactoring
{
    public static class App
    {
        public static void ApplyDiscount(CartId cartId, IStorage<Cart> storage)
        {
            var cart = LoadCart(cartId);
            if (cart != Cart.MissingCart)
            {
                var rule = LookupDiscountRule(cart.CustomerId);
                if (rule != DiscountRule.NoDiscount)
                {
                    var discount = rule.Compute(cart);
                    var updatedCart = UpdateAmount(cart, discount);
                    Save(updatedCart, storage);
                }
            }
        }

        static Cart LoadCart(CartId id)
        {
            if (id.Value.Contains("gold"))
                return new Cart(id, new CustomerId("gold-customer"), new Amount(100));
            if (id.Value.Contains("normal"))
                return new Cart(id, new CustomerId("normal-customer"), new Amount(100));
            return Cart.MissingCart;
        }

        static DiscountRule LookupDiscountRule(CustomerId id)
        {
            if (id.Value.Contains("gold")) return new DiscountRule(Half);
            return DiscountRule.NoDiscount;
        }

        static Cart UpdateAmount(Cart cart, Amount discount)
        {
            return new Cart(cart.Id, cart.CustomerId, new Amount(cart.Amount.Value - discount.Value));
        }

        static void Save(Cart cart, IStorage<Cart> storage)
        {
            storage.Flush(cart);
        }

        static Amount Half(Cart cart) =>
            new Amount(cart.Amount.Value / 2);
    }
}