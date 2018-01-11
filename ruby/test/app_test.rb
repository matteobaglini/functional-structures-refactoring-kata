require "minitest/autorun"
require "app"

class ModelsTest < Minitest::Test
    def test_happy_path
        cartId = CartId.new("some-gold-cart")
        storage = SpyStorage.new

        App.new.apply_discount(cartId, storage)

        expected = Cart.new(CartId.new("some-gold-cart"), CustomerId.new("gold-customer"), Amount.new(50))
        assert_equal expected, storage.saved
    end

    def test_missing_cart
        cartId = CartId.new("missing-cart")
        storage = SpyStorage.new

        App.new.apply_discount(cartId, storage)

        assert_nil storage.saved
    end

    def test_no_discount
        cartId = CartId.new("some-normal-cart")
        storage = SpyStorage.new

        App.new.apply_discount(cartId, storage)

        assert_nil storage.saved
    end

    class SpyStorage
        attr_reader :saved

        def flush(cart)
            @saved = cart
        end
    end
end