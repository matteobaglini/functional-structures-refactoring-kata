using System;

namespace FunctionalRefactoring.Models
{
    public class Amount
    {
        public Decimal Value { get; }

        public Amount(Decimal value) => Value = value;

        Boolean Equals(Amount other) => Value == other.Value;

        public override Boolean Equals(Object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != typeof(Amount)) return false;
            return Equals((Amount) obj);
        }

        public override Int32 GetHashCode() => Value.GetHashCode();

        public static Boolean operator ==(Amount left, Amount right) => Equals(left, right);

        public static Boolean operator !=(Amount left, Amount right) => !Equals(left, right);
    }
}