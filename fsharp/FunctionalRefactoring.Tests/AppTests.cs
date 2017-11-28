using Xunit;
using static FunctionalRefactoring.Models;

namespace FunctionalRefactoring.Tests
{
    public class AppTests
    {
        [Fact]
        public void HappyPath()
        {
            var cartId = CartId.NewCartId("some-gold-cart");
            var storage = new SpyStorage();

            App.ApplyDiscount(cartId, storage);

            var expected = new Cart(
                CartId.NewCartId("some-gold-cart"), 
                CustomerId.NewCustomerId("gold-customer"), 
                Amount.NewAmount(50));

            Assert.Equal(expected, storage.Saved);
        }

        [Fact]
        public void NoDiscount()
        {
            var cartId = CartId.NewCartId("some-normal-cart");
            var storage = new SpyStorage();

            App.ApplyDiscount(cartId, storage);

            Assert.Null(storage.Saved);
        }

        [Fact]
        public void MissingCart()
        {
            var cartId = CartId.NewCartId("missing-cart");
            var storage = new SpyStorage();

            App.ApplyDiscount(cartId, storage);

            Assert.Null(storage.Saved);
        }

        class SpyStorage : IStorage<Cart>
        {
            public Cart Saved { get; private set; }

            public void Flush(Cart item)
            {
                Saved = item;
            }
        }
    }
}