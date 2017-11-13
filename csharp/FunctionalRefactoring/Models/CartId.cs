using System;

namespace FunctionalRefactoring.Models
{
    public class CartId
    {
        public String Value { get; }

        public CartId(String value) => Value = value;

        Boolean Equals(CartId other) => String.Equals(Value, other.Value);

        public override Boolean Equals(Object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != typeof(CartId)) return false;
            return Equals((CartId)obj);
        }

        public override Int32 GetHashCode() => Value != null ? Value.GetHashCode() : 0;

        public static Boolean operator ==(CartId left, CartId right) => Equals(left, right);

        public static Boolean operator !=(CartId left, CartId right) => !Equals(left, right);
    }
}