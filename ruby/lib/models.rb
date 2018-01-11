
class Amount
    attr_reader :value

    def initialize(value)
        @value = value
    end

    def ==(o)
        o.class == self.class && o.value == value
    end
end

class CustomerId
    attr_reader :value

    def initialize(value)
        @value = value
    end

    def ==(o)
        o.class == self.class && o.value == value
    end
end

class CartId
    attr_reader :value

    def initialize(value)
        @value = value
    end

    def ==(o)
        o.class == self.class && o.value == value
    end
end

class Cart
    attr_reader :id
    attr_reader :customerId
    attr_reader :amount

    def self.missing_cart
        @@missing_cart ||= Cart.new(CartId.new(""), CustomerId.new(""), Amount.new(0))
    end

    def initialize(id, customerId, amount)
        @id = id
        @customerId = customerId
        @amount = amount
    end

    def ==(o)
        o.class == self.class && o.state == state
    end

    protected

    def state
        [@id, @customerId, @amount]
    end
end

class DiscountRule < Proc
    def self.no_discount
        @@no_discount ||= DiscountRule.new { raise "no discount" }
    end
end