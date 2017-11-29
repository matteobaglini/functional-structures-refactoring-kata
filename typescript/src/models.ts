

export type Amount = number

export type CartId = string

export type CustomerId = string

export type DiscountRule = (Cart) => Amount

export interface Cart {
  id: CartId,
  customerId: CustomerId,
  amount: Amount
}

export const MissingCart: Cart = {
  id: "",
  customerId: "",
  amount: 0
}

export const NoDiscount: DiscountRule = (c: Cart): Amount => {
  throw 'IllegalStateException'
}
