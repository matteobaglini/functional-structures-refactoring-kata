using FunctionalRefactoring.Models;
using Xunit;

namespace FunctionalRefactoring.Tests
{
    public class AppTests
    {
        [Fact]
        public void HappyPath()
        {
            var cartId = new CartId("some-gold-cart");
            var storage = new SpyStorage();

            App.ApplyDiscount(cartId, storage);

            var expected = new Cart(new CartId("some-gold-cart"), new CustomerId("gold-customer"), new Amount(50));

            Assert.Equal(expected, storage.Saved);
        }

        [Fact]
        public void NoDiscount()
        {
            var cartId = new CartId("some-normal-cart");
            var storage = new SpyStorage();

            App.ApplyDiscount(cartId, storage);

            Assert.Null(storage.Saved);
        }

        [Fact]
        public void MissingCart()
        {
            var cartId = new CartId("missing-cart");
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