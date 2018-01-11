class App
    def apply_discount(cartId, storage)
        cart = load_cart(cartId)
        if (cart != Cart.missing_cart)
            rule = lookup_discount_rule(cart.customerId)
            if (rule != DiscountRule.no_discount)
                discount = rule.call(cart)
                updated_cart = update_amount(cart, discount)
                save(updated_cart, storage)
            end
        end
    end

    private

    def load_cart(id)
        if(id.value.include?("gold")) 
            Cart.new(id, CustomerId.new("gold-customer"), Amount.new(100))
        elsif(id.value.include?("normal")) 
            Cart.new(id, CustomerId.new("normal-customer"), Amount.new(100))
        else 
            Cart.missing_cart
        end
    end

    def lookup_discount_rule(customerId)
        if (customerId.value.include?("gold")) 
            DiscountRule.new(&method(:half))
        else 
            DiscountRule.no_discount
        end
    end

    def half(cart)
        Amount.new(cart.amount.value / 2)
    end

    def update_amount(cart, discount)
        Cart.new(cart.id, cart.customerId, Amount.new(cart.amount.value - discount.value))
    end

    def save(cart, storage)
        storage.flush(cart)
    end
end