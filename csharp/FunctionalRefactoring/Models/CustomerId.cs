using System;

namespace FunctionalRefactoring.Models
{
    public class CustomerId
    {
        public String Value { get; }

        public CustomerId(String value) => Value = value;

        Boolean Equals(CustomerId other) => String.Equals(Value, other.Value);

        public override Boolean Equals(Object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != typeof(CustomerId)) return false;
            return Equals((CustomerId)obj);
        }

        public override Int32 GetHashCode() => Value != null ? Value.GetHashCode() : 0;

        public static Boolean operator ==(CustomerId left, CustomerId right) => Equals(left, right);

        public static Boolean operator !=(CustomerId left, CustomerId right) => !Equals(left, right);
    }
}