using System;

namespace FunctionalRefactoring.Models
{
    public class Cart
    {
        public static readonly Cart MissingCart = new Cart(new CartId(""), new CustomerId(""), new Amount(0));

        public CartId Id { get; }
        public CustomerId CustomerId { get; }
        public Amount Amount { get; }

        public Cart(CartId id, CustomerId customerId, Amount amount)
        {
            Id = id;
            CustomerId = customerId;
            Amount = amount;
        }

        Boolean Equals(Cart other) => Equals(Id, other.Id)
                                      && Equals(CustomerId, other.CustomerId)
                                      && Equals(Amount, other.Amount);

        public override Boolean Equals(Object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != typeof(Cart)) return false;
            return Equals((Cart)obj);
        }

        public override Int32 GetHashCode()
        {
            unchecked
            {
                var hashCode = Id != null ? Id.GetHashCode() : 0;
                hashCode = (hashCode * 397) ^ (CustomerId != null ? CustomerId.GetHashCode() : 0);
                hashCode = (hashCode * 397) ^ (Amount != null ? Amount.GetHashCode() : 0);
                return hashCode;
            }
        }

        public static Boolean operator ==(Cart left, Cart right) => Equals(left, right);

        public static Boolean operator !=(Cart left, Cart right) => !Equals(left, right);
    }
}