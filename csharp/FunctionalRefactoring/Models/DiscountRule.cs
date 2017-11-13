using System;

namespace FunctionalRefactoring.Models
{
    public class DiscountRule
    {
        public static readonly DiscountRule NoDiscount = new DiscountRule(c => throw new InvalidOperationException("no discount"));

        public Func<Cart, Amount> Compute { get; }

        public DiscountRule(Func<Cart, Amount> compute) => Compute = compute;

        Boolean Equals(DiscountRule other) => Equals(Compute, other.Compute);

        public override Boolean Equals(Object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != typeof(DiscountRule)) return false;
            return Equals((DiscountRule)obj);
        }

        public override Int32 GetHashCode()
        {
            return Compute != null ? Compute.GetHashCode() : 0;
        }

        public static Boolean operator ==(DiscountRule left, DiscountRule right) => Equals(left, right);

        public static Boolean operator !=(DiscountRule left, DiscountRule right) => !Equals(left, right);
    }
}